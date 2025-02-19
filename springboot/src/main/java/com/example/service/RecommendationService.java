package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.entity.Comment;
import com.example.entity.Film;
import com.example.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    @Autowired
    private FilmService filmService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // 获取用户评分矩阵并填充缺失评分
    public Map<Integer, Map<Integer, Double>> getUserFilmRatings() {
        List<Score> scoreList = userService.getAllScores(); // 获取所有评分
        Map<Integer, Map<Integer, Double>> userFilmRatings = new HashMap<>();
        Map<Integer, Double> filmAverageRatings = new HashMap<>();

        // 获取电影的平均评分
        for (Score score : scoreList) {
            filmAverageRatings.put(score.getFilmId(),
                    filmAverageRatings.getOrDefault(score.getFilmId(), 0.0) + score.getScore());
        }

        for (Map.Entry<Integer, Double> entry : filmAverageRatings.entrySet()) {
            filmAverageRatings.put(entry.getKey(), entry.getValue() / scoreList.size());
        }

        // 填充评分数据
        for (Score score : scoreList) {
            userFilmRatings
                    .computeIfAbsent(score.getUserId(), k -> new HashMap<>())
                    .put(score.getFilmId(), score.getScore());
        }

        // 填充缺失评分
        for (Map.Entry<Integer, Map<Integer, Double>> userEntry : userFilmRatings.entrySet()) {
            Map<Integer, Double> userRatings = userEntry.getValue();
            for (Map.Entry<Integer, Double> filmEntry : filmAverageRatings.entrySet()) {
                if (!userRatings.containsKey(filmEntry.getKey())) {
                    userRatings.put(filmEntry.getKey(), filmEntry.getValue());  // 填充为电影平均评分
                }
            }
        }

        return userFilmRatings;
    }

    // 基于相似度预测评分
    public double predictRating(int targetUserId, int filmId, Map<Integer, Map<Integer, Double>> userFilmRatings) {
        Map<Integer, Double> targetUserRatings = userFilmRatings.get(targetUserId);

        // 计算加权评分
        double numerator = 0.0;
        double denominator = 0.0;

        for (Map.Entry<Integer, Map<Integer, Double>> entry : userFilmRatings.entrySet()) {
            int otherUserId = entry.getKey();
            if (otherUserId != targetUserId && entry.getValue().containsKey(filmId)) {
                double similarity = calculateCosineSimilarity(targetUserRatings, entry.getValue());
                numerator += similarity * entry.getValue().get(filmId);
                denominator += Math.abs(similarity);
            }
        }

        if (denominator == 0) {
            return 0;  // 如果没有相似用户，返回 0 或者电影的平均评分
        }

        return numerator / denominator;
    }

    // 计算余弦相似度
    private double calculateCosineSimilarity(Map<Integer, Double> userRatings1, Map<Integer, Double> userRatings2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Map.Entry<Integer, Double> entry : userRatings1.entrySet()) {
            Integer filmId = entry.getKey();
            if (userRatings2.containsKey(filmId)) {
                dotProduct += entry.getValue() * userRatings2.get(filmId);
                norm1 += Math.pow(entry.getValue(), 2);
                norm2 += Math.pow(userRatings2.get(filmId), 2);
            }
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // 获取电影推荐列表
    public List<Film> recommendFilmsByUser(Integer userId) {
        // 获取用户的历史收藏电影
        List<Film> collectedFilms = filmService.getFilmsByUserId(userId);
        // 获取用户的评论
        List<Comment> userComments = commentService.selectByUserId(userId);

        // 获取电影分类（根据收藏的电影）
        Map<Integer, Long> filmCategories = new HashMap<>();
        for (Film film : collectedFilms) {
            List<Integer> typeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
            for (Integer typeId : typeIds) {
                filmCategories.put(typeId, filmCategories.getOrDefault(typeId, 0L) + 1);
            }
        }

        // 排序分类，优先推荐收藏最多的类别
        List<Map.Entry<Integer, Long>> sortedCategories = new ArrayList<>(filmCategories.entrySet());
        sortedCategories.sort((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()));

        List<Film> recommendedFilms = new ArrayList<>();
        for (Map.Entry<Integer, Long> categoryEntry : sortedCategories) {
            // 获取该分类下的电影
            List<Film> filmsInCategory = filmService.getFilmsByCategory(categoryEntry.getKey());
            recommendedFilms.addAll(filmsInCategory);

            // 如果已推荐的电影不足 8 部，则继续推荐其他类别
            if (recommendedFilms.size() >= 8) {
                break;
            }
        }

        // 如果推荐的电影不足 8 部，基于协同过滤进一步推荐
        if (recommendedFilms.size() < 8) {
            Map<Integer, Map<Integer, Double>> userFilmRatings = getUserFilmRatings();
            for (Film film : filmService.selectAll(new Film())) {
                if (!recommendedFilms.contains(film)) {
                    double predictedRating = predictRating(userId, film.getId(), userFilmRatings);
                    if (predictedRating > 3.5) { // 设置一个阈值，例如预测评分大于3.5
                        recommendedFilms.add(film);
                    }
                    if (recommendedFilms.size() >= 8) {
                        break;
                    }
                }
            }
        }

        // 如果推荐的电影不足 8 部，从电影库中随机推荐
        if (recommendedFilms.size() < 8) {
            List<Film> randomFilms = filmService.selectAll(new Film());
            Collections.shuffle(randomFilms);
            recommendedFilms.addAll(randomFilms.subList(0, 8 - recommendedFilms.size()));
        }

        return recommendedFilms;
    }
}
