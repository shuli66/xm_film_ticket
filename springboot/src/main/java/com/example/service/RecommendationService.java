package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.entity.Comment;
import com.example.entity.Film;
import com.example.entity.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

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
            logger.info("用户 {} 没有收藏电影，无法进行基于内容的推荐", userId);
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

        logger.info("用户 {} 基于内容推荐获取到 {} 部电影", userId, recommendations.size());
        return recommendations;
    }

    // 综合推荐方法
    public List<Film> recommendFilmsByUser(Integer userId) {
        logger.info("开始为用户 {} 生成电影推荐", userId);
        List<Film> allFilms = filmService.selectAll(new Film());
        if (allFilms == null || allFilms.isEmpty()) {
            logger.warn("系统中没有可用的电影数据");
            return new ArrayList<>();
        }
        logger.info("系统中共有 {} 部电影可供推荐", allFilms.size());
        
        Map<Integer, Map<Integer, Double>> userFilmRatings = getUserFilmRatings();
        
        // 1. 获取基于内容的推荐
        List<Film> contentBasedRecommendations = getContentBasedRecommendations(userId, allFilms);
        
        // 处理新用户情况：如果无法获取基于内容的推荐（因为用户没有收藏电影）
        // 则提供热门推荐（评分最高的电影）
        if (contentBasedRecommendations.isEmpty()) {
            logger.info("用户 {} 是新用户或没有收藏记录，尝试获取评分最高的电影", userId);
            List<Film> topFilms = filmService.selectScoreTop10();
            if (topFilms != null && !topFilms.isEmpty()) {
                // 将评分最高的电影添加到推荐中
                logger.info("为新用户 {} 添加 {} 部评分最高的电影", userId, topFilms.size());
                contentBasedRecommendations.addAll(topFilms);
            } else {
                logger.warn("未能获取评分最高的电影数据");
            }
        }
        
        // 2. 获取协同过滤推荐
        List<Film> collaborativeRecommendations = new ArrayList<>();
        Map<Integer, Double> filmScores = new HashMap<>();
        
        // 检查用户是否存在于评分矩阵中（新用户可能不存在）
        boolean userHasRatings = userFilmRatings.containsKey(userId) && !userFilmRatings.get(userId).isEmpty();
        logger.info("用户 {} 是否有评分记录: {}", userId, userHasRatings);
        
        if (userHasRatings) {
            logger.info("尝试为用户 {} 生成协同过滤推荐", userId);
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
            logger.info("协同过滤推荐获取到 {} 部电影", collaborativeRecommendations.size());
        }
        
        // 3. 合并推荐结果并排序
        List<Film> finalRecommendations = new ArrayList<>(contentBasedRecommendations);
        
        // 按预测评分排序协同过滤推荐结果
        collaborativeRecommendations.sort((f1, f2) -> 
            Double.compare(filmScores.get(f2.getId()), filmScores.get(f1.getId())));
        
        finalRecommendations.addAll(collaborativeRecommendations);
        logger.info("综合推荐初步结果: {} 部电影", finalRecommendations.size());
        
        // 如果综合推荐结果仍然为空，添加票房排行榜和高评分电影
        if (finalRecommendations.isEmpty()) {
            logger.info("推荐结果为空，尝试添加票房排行榜电影");
            // 添加票房排行榜电影
            List<Film> popularFilms = filmService.selectTotalTop10();
            if (popularFilms != null && !popularFilms.isEmpty()) {
                logger.info("添加 {} 部票房排行榜电影", popularFilms.size());
                finalRecommendations.addAll(popularFilms);
            } else {
                logger.warn("未能获取票房排行榜电影数据");
            }
            
            // 如果仍然没有推荐，添加评分最高的电影（可能与之前的 topFilms 重复，但为了确保有推荐）
            if (finalRecommendations.isEmpty()) {
                logger.info("尝试添加评分最高的电影");
                List<Film> topRatedFilms = filmService.selectScoreTop10();
                if (topRatedFilms != null && !topRatedFilms.isEmpty()) {
                    logger.info("添加 {} 部评分最高的电影", topRatedFilms.size());
                    finalRecommendations.addAll(topRatedFilms);
                } else {
                    logger.warn("未能获取评分最高的电影数据");
                }
            }
            
            // 如果经过所有尝试后仍然没有推荐，则返回所有可用的电影
            if (finalRecommendations.isEmpty() && !allFilms.isEmpty()) {
                // 限制返回数量，避免返回过多电影
                logger.info("作为最后手段，添加前10部可用电影");
                finalRecommendations.addAll(allFilms.stream()
                        .limit(10)
                        .collect(Collectors.toList()));
            }
        }
        
        // 确保新用户一定有推荐结果
        if (finalRecommendations.isEmpty()) {
            logger.warn("所有推荐尝试均失败，返回前8部电影作为默认推荐");
            return allFilms.stream()
                    .limit(8)
                    .collect(Collectors.toList());
        }
        
        // 4. 限制推荐数量
        final int MAX_RECOMMENDATIONS = 8;
        List<Film> result = finalRecommendations.stream()
                .limit(MAX_RECOMMENDATIONS)
                .collect(Collectors.toList());
        
        logger.info("最终为用户 {} 推荐 {} 部电影", userId, result.size());
        return result;
    }
}
