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

    //    // 计算皮尔逊相关系数
    //    private double calculatePearsonCorrelation(Map<Integer, Double> userRatings1,
    //                                             Map<Integer, Double> userRatings2) {
    //        Set<Integer> commonFilms = new HashSet<>(userRatings1.keySet());
    //        commonFilms.retainAll(userRatings2.keySet());
    //
    //        if (commonFilms.isEmpty()) {
    //            return 0.0;
    //        }
    //
    //        double sum1 = 0.0, sum2 = 0.0;
    //        double sum1Sq = 0.0, sum2Sq = 0.0;
    //        double pSum = 0.0;
    //        int n = commonFilms.size();
    //
    //        for (Integer filmId : commonFilms) {
    //            double rating1 = userRatings1.get(filmId);
    //            double rating2 = userRatings2.get(filmId);
    //
    //            sum1 += rating1;
    //            sum2 += rating2;
    //            sum1Sq += rating1 * rating1;
    //            sum2Sq += rating2 * rating2;
    //            pSum += rating1 * rating2;
    //        }
    //
    //        double num = pSum - (sum1 * sum2 / n);
    //        double den = Math.sqrt((sum1Sq - sum1 * sum1 / n) * (sum2Sq - sum2 * sum2 / n));
    //
    //        return den == 0 ? 0 : num / den;
    //    }

        // 计算余弦相似度（公式2.1）
        private double calculateCosineSimilarity(Map<Integer, Double> userRatings1,
                                              Map<Integer, Double> userRatings2) {
            Set<Integer> commonFilms = new HashSet<>(userRatings1.keySet());
            commonFilms.retainAll(userRatings2.keySet());

            if (commonFilms.isEmpty()) {
                return 0.0;
            }

            double dotProduct = 0.0;
            double norm1 = 0.0;
            double norm2 = 0.0;

            for (Integer filmId : commonFilms) {
                double rating1 = userRatings1.get(filmId);
                double rating2 = userRatings2.get(filmId);

                dotProduct += rating1 * rating2;
                norm1 += rating1 * rating1;
                norm2 += rating2 * rating2;
            }

            // 计算向量的模长乘积
            double normProduct = Math.sqrt(norm1) * Math.sqrt(norm2);

            return normProduct == 0 ? 0 : dotProduct / normProduct;
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
                    // 使用余弦相似度代替皮尔逊相关系数
                    double similarity = calculateCosineSimilarity(targetUserRatings, entry.getValue());
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

            // 计算加权评分（公式2.2）
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

        // 获取热门电影推荐（针对冷启动问题）
        private List<Film> getPopularFilmRecommendations(List<Film> allFilms) {
            // 获取评分高且评分人数多的电影
            Map<Film, Double> filmPopularity = new HashMap<>();

            for (Film film : allFilms) {
                // 根据评分和评分人数综合计算流行度
                double avgScore = film.getScore() != null ? film.getScore() : 0;
                // 使用评论服务获取评论数量
                long commentCount = commentService.countByFilmId(film.getId());

                // 票房也是衡量热门程度的指标
                double boxOffice = film.getTotal() != null ? film.getTotal() : 0;

                // 综合指标计算热门度
                double popularity = avgScore * 0.5 + (commentCount > 0 ? Math.log10(commentCount + 1) : 0) * 0.3
                                  + (boxOffice > 0 ? Math.log10(boxOffice + 1) * 0.2 : 0);

                filmPopularity.put(film, popularity);
            }

            // 按热门度排序
            return filmPopularity.entrySet().stream()
                    .sorted(Map.Entry.<Film, Double>comparingByValue().reversed())
                    .limit(10)  // 获取前10部热门电影
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        // 获取多样化推荐（针对冷启动问题）
        private List<Film> getDiversifiedRecommendations(List<Film> allFilms) {
            List<Film> diversifiedFilms = new ArrayList<>();

            // 按类型分组
            Map<Integer, List<Film>> filmsByType = new HashMap<>();

            for (Film film : allFilms) {
                List<Integer> typeIds = JSONUtil.toList(film.getTypeIds(), Integer.class);
                for (Integer typeId : typeIds) {
                    filmsByType.computeIfAbsent(typeId, k -> new ArrayList<>()).add(film);
                }
            }

            // 从每个类型中选取评分最高的电影
            for (Map.Entry<Integer, List<Film>> entry : filmsByType.entrySet()) {
                List<Film> films = entry.getValue();
                films.sort((f1, f2) -> Double.compare(
                        f2.getScore() != null ? f2.getScore() : 0,
                        f1.getScore() != null ? f1.getScore() : 0));

                if (!films.isEmpty()) {
                    diversifiedFilms.add(films.get(0));
                }
            }

            // 如果推荐数量不足，添加一些评分高的电影
            if (diversifiedFilms.size() < 8) {
                List<Film> highRatedFilms = allFilms.stream()
                        .sorted((f1, f2) -> Double.compare(
                                f2.getScore() != null ? f2.getScore() : 0,
                                f1.getScore() != null ? f1.getScore() : 0))
                        .limit(8 - diversifiedFilms.size())
                        .collect(Collectors.toList());

                // 添加不重复的电影
                for (Film film : highRatedFilms) {
                    if (diversifiedFilms.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                        diversifiedFilms.add(film);
                    }
                }
            }

            return diversifiedFilms;
        }

        // 获取随机电影推荐
        private List<Film> getRandomRecommendations(List<Film> allFilms, int count) {
            if (allFilms == null || allFilms.isEmpty()) {
                return new ArrayList<>();
            }

            // 创建一个电影列表的副本
            List<Film> filmsCopy = new ArrayList<>(allFilms);
            // 随机打乱顺序
            Collections.shuffle(filmsCopy);

            // 返回指定数量的电影
            return filmsCopy.stream()
                    .limit(count)
                    .collect(Collectors.toList());
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

            // 处理新用户情况
            boolean isNewUser = contentBasedRecommendations.isEmpty();

            // 如果是新用户，获取热门推荐和多样化推荐
            if (isNewUser) {
                logger.info("用户 {} 是新用户或没有收藏记录，应用冷启动策略", userId);

                // 获取热门推荐
                List<Film> popularRecommendations = getPopularFilmRecommendations(allFilms);
                logger.info("为新用户 {} 获取了 {} 部热门电影", userId, popularRecommendations.size());

                // 获取多样化推荐
                List<Film> diversifiedRecommendations = getDiversifiedRecommendations(allFilms);
                logger.info("为新用户 {} 获取了 {} 部多样化电影", userId, diversifiedRecommendations.size());

                // 合并热门和多样化推荐，确保没有重复
                List<Film> combinedRecommendations = new ArrayList<>(popularRecommendations);

                for (Film film : diversifiedRecommendations) {
                    if (combinedRecommendations.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                        combinedRecommendations.add(film);
                    }
                }

                // 使用评分最高的电影填充
                List<Film> topFilms = filmService.selectScoreTop10();
                if (topFilms != null && !topFilms.isEmpty()) {
                    for (Film film : topFilms) {
                        if (combinedRecommendations.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                            combinedRecommendations.add(film);
                        }
                    }
                }

                // 如果推荐数量仍然不足，添加随机推荐
                final int MAX_RECOMMENDATIONS = 8;
                if (combinedRecommendations.size() < MAX_RECOMMENDATIONS) {
                    logger.info("新用户推荐数量不足，添加随机推荐");
                    List<Film> randomFilms = getRandomRecommendations(allFilms, MAX_RECOMMENDATIONS);
                    for (Film film : randomFilms) {
                        if (combinedRecommendations.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                            combinedRecommendations.add(film);
                            if (combinedRecommendations.size() >= MAX_RECOMMENDATIONS) {
                                break;
                            }
                        }
                    }
                }

                logger.info("为新用户 {} 最终组合了 {} 部推荐电影", userId, combinedRecommendations.size());

                // 限制推荐数量并返回
                return combinedRecommendations.stream()
                        .limit(MAX_RECOMMENDATIONS)
                        .collect(Collectors.toList());
            }

            // 2. 获取协同过滤推荐
            List<Film> collaborativeRecommendations = new ArrayList<>();
            Map<Integer, Double> filmScores = new HashMap<>();

            // 检查用户是否存在于评分矩阵中
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

            // 如果综合推荐结果仍然为空，添加热门和多样化推荐
            if (finalRecommendations.isEmpty()) {
                logger.info("推荐结果为空，应用冷启动策略");
                List<Film> coldStartRecommendations = new ArrayList<>();

                // 添加热门推荐
                List<Film> popularFilms = getPopularFilmRecommendations(allFilms);
                if (popularFilms != null && !popularFilms.isEmpty()) {
                    logger.info("添加 {} 部热门电影", popularFilms.size());
                    coldStartRecommendations.addAll(popularFilms);
                }

                // 添加多样化推荐
                List<Film> diversifiedFilms = getDiversifiedRecommendations(allFilms);
                if (diversifiedFilms != null && !diversifiedFilms.isEmpty()) {
                    logger.info("添加 {} 部多样化电影", diversifiedFilms.size());
                    for (Film film : diversifiedFilms) {
                        if (coldStartRecommendations.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                            coldStartRecommendations.add(film);
                        }
                    }
                }

                finalRecommendations = coldStartRecommendations;
            }

            // 如果仍然没有推荐结果，使用随机推荐
            final int MAX_RECOMMENDATIONS = 8;
            if (finalRecommendations.isEmpty()) {
                logger.info("所有推荐策略均无结果，使用随机推荐");
                finalRecommendations = getRandomRecommendations(allFilms, MAX_RECOMMENDATIONS);
                logger.info("随机推荐了 {} 部电影", finalRecommendations.size());
            }

            // 如果推荐数量不足8部，使用随机推荐补充
            if (finalRecommendations.size() < MAX_RECOMMENDATIONS) {
                logger.info("推荐数量不足8部，使用随机推荐补充");
                List<Film> additionalRandomFilms = getRandomRecommendations(allFilms, MAX_RECOMMENDATIONS);
                for (Film film : additionalRandomFilms) {
                    if (finalRecommendations.stream().noneMatch(f -> f.getId().equals(film.getId()))) {
                        finalRecommendations.add(film);
                        if (finalRecommendations.size() >= MAX_RECOMMENDATIONS) {
                            break;
                        }
                    }
                }
            }

            // 4. 限制推荐数量
            List<Film> result = finalRecommendations.stream()
                    .limit(MAX_RECOMMENDATIONS)
                    .collect(Collectors.toList());

            logger.info("最终为用户 {} 推荐 {} 部电影", userId, result.size());
            return result;
        }
    }
