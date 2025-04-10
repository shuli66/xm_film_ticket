package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.entity.Comment;
import com.example.entity.Film;
import com.example.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private FilmService filmService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // 获取用户评分矩阵
    public Map<Integer, Map<Integer, Double>> getUserFilmRatings() {
        List<Score> scoreList = userService.getAllScores();
        Map<Integer, Map<Integer, Double>> userFilmRatings = new HashMap<>();
        
        // 计算每部电影的平均评分和标准差
        Map<Integer, Double> filmAverageRatings = new HashMap<>();
        Map<Integer, Double> filmStdDevs = new HashMap<>();
        Map<Integer, Integer> filmRatingCounts = new HashMap<>();
        
        // 计算每部电影的总评分和评分次数
        for (Score score : scoreList) {
            int filmId = score.getFilmId();
            filmAverageRatings.put(filmId, 
                    filmAverageRatings.getOrDefault(filmId, 0.0) + score.getScore());
            filmRatingCounts.put(filmId, 
                    filmRatingCounts.getOrDefault(filmId, 0) + 1);
        }
        
        // 计算每部电影的平均评分
        for (Integer filmId : filmAverageRatings.keySet()) {
            filmAverageRatings.put(filmId, 
                    filmAverageRatings.get(filmId) / filmRatingCounts.get(filmId));
        }
        
        // 计算标准差
        for (Score score : scoreList) {
            int filmId = score.getFilmId();
            double diff = score.getScore() - filmAverageRatings.get(filmId);
            filmStdDevs.put(filmId, 
                    filmStdDevs.getOrDefault(filmId, 0.0) + diff * diff);
        }
        
        for (Integer filmId : filmStdDevs.keySet()) {
            filmStdDevs.put(filmId, 
                    Math.sqrt(filmStdDevs.get(filmId) / filmRatingCounts.get(filmId)));
        }

        // 填充用户的实际评分数据
        for (Score score : scoreList) {
            userFilmRatings
                    .computeIfAbsent(score.getUserId(), k -> new HashMap<>())
                    .put(score.getFilmId(), score.getScore());
        }

        return userFilmRatings;
    }

    // 计算皮尔逊相关系数
    private double calculatePearsonCorrelation(Map<Integer, Double> userRatings1, 
                                             Map<Integer, Double> userRatings2) {
        Set<Integer> commonFilms = new HashSet<>(userRatings1.keySet());
        commonFilms.retainAll(userRatings2.keySet());
        
        if (commonFilms.isEmpty()) {
            return 0.0;
        }

        double sum1 = 0.0, sum2 = 0.0;
        double sum1Sq = 0.0, sum2Sq = 0.0;
        double pSum = 0.0;
        int n = commonFilms.size();

        for (Integer filmId : commonFilms) {
            double rating1 = userRatings1.get(filmId);
            double rating2 = userRatings2.get(filmId);
            
            sum1 += rating1;
            sum2 += rating2;
            sum1Sq += rating1 * rating1;
            sum2Sq += rating2 * rating2;
            pSum += rating1 * rating2;
        }

        double num = pSum - (sum1 * sum2 / n);
        double den = Math.sqrt((sum1Sq - sum1 * sum1 / n) * (sum2Sq - sum2 * sum2 / n));
        
        return den == 0 ? 0 : num / den;
    }

    // 预测评分
    public double predictRating(int targetUserId, int filmId, 
                              Map<Integer, Map<Integer, Double>> userFilmRatings) {
        Map<Integer, Double> targetUserRatings = userFilmRatings.get(targetUserId);
        if (targetUserRatings == null) {
            return 0.0;
        }

        List<Map.Entry<Integer, Double>> similarUsers = new ArrayList<>();
        
        // 计算用户相似度
        for (Map.Entry<Integer, Map<Integer, Double>> entry : userFilmRatings.entrySet()) {
            int otherUserId = entry.getKey();
            if (otherUserId != targetUserId && entry.getValue().containsKey(filmId)) {
                double similarity = calculatePearsonCorrelation(targetUserRatings, entry.getValue());
                if (similarity > 0) {
                    similarUsers.add(new AbstractMap.SimpleEntry<>(otherUserId, similarity));
                }
            }
        }
        
        // 选取相似度最高的前N个用户
        final int MAX_SIMILAR_USERS = 5;
        similarUsers.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
        int userLimit = Math.min(similarUsers.size(), MAX_SIMILAR_USERS);
        similarUsers = similarUsers.subList(0, userLimit);
        
        if (similarUsers.isEmpty()) {
            return userFilmRatings.values().stream()
                    .filter(ratings -> ratings.containsKey(filmId))
                    .mapToDouble(ratings -> ratings.get(filmId))
                    .average()
                    .orElse(5.0);
        }

        // 计算加权评分
        double numerator = 0.0;
        double denominator = 0.0;

        for (Map.Entry<Integer, Double> entry : similarUsers) {
            int otherUserId = entry.getKey();
            double similarity = entry.getValue();
            double rating = userFilmRatings.get(otherUserId).get(filmId);
            numerator += similarity * rating;
            denominator += Math.abs(similarity);
        }

        return denominator == 0 ? 0.0 : numerator / denominator;
    }

    // 获取基于内容的推荐
    private List<Film> getContentBasedRecommendations(Integer userId, List<Film> allFilms) {
        List<Film> recommendations = new ArrayList<>();
        
        // 获取用户收藏的电影
        List<Film> userCollectedFilms = filmService.getFilmsByUserId(userId);
        if (userCollectedFilms.isEmpty()) {
            return recommendations;
        }

        // 提取用户偏好特征
        Set<Integer> preferredTypes = new HashSet<>();
        Set<Integer> preferredAreas = new HashSet<>();
        
        for (Film film : userCollectedFilms) {
            List<Integer> typeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
            preferredTypes.addAll(typeIds);
            preferredAreas.add(film.getAreaId());
        }

        // 基于用户偏好推荐电影
        for (Film film : allFilms) {
            if (userCollectedFilms.stream().anyMatch(f -> f.getId().equals(film.getId()))) {
                continue; // 跳过已收藏的电影
            }

            List<Integer> filmTypeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
            boolean hasPreferredType = filmTypeIds.stream().anyMatch(preferredTypes::contains);
            boolean hasPreferredArea = preferredAreas.contains(film.getAreaId());

            if (hasPreferredType || hasPreferredArea) {
                recommendations.add(film);
            }
        }

        return recommendations;
    }

    // 综合推荐方法
    public List<Film> recommendFilmsByUser(Integer userId) {
        List<Film> allFilms = filmService.selectAll(new Film());
        Map<Integer, Map<Integer, Double>> userFilmRatings = getUserFilmRatings();
        
        // 1. 获取基于内容的推荐
        List<Film> contentBasedRecommendations = getContentBasedRecommendations(userId, allFilms);
        
        // 2. 获取协同过滤推荐
        List<Film> collaborativeRecommendations = new ArrayList<>();
        Map<Integer, Double> filmScores = new HashMap<>();
        
        for (Film film : allFilms) {
            if (contentBasedRecommendations.stream().anyMatch(f -> f.getId().equals(film.getId()))) {
                continue; // 跳过基于内容推荐的电影
            }
            
            double predictedScore = predictRating(userId, film.getId(), userFilmRatings);
            if (predictedScore > 0) {
                filmScores.put(film.getId(), predictedScore);
                collaborativeRecommendations.add(film);
            }
        }
        
        // 3. 合并推荐结果并排序
        List<Film> finalRecommendations = new ArrayList<>(contentBasedRecommendations);
        
        // 按预测评分排序协同过滤推荐结果
        collaborativeRecommendations.sort((f1, f2) -> 
            Double.compare(filmScores.get(f2.getId()), filmScores.get(f1.getId())));
        
        finalRecommendations.addAll(collaborativeRecommendations);
        
        // 4. 限制推荐数量
        final int MAX_RECOMMENDATIONS = 8;
        return finalRecommendations.stream()
                .limit(MAX_RECOMMENDATIONS)
                .collect(Collectors.toList());
    }
}
