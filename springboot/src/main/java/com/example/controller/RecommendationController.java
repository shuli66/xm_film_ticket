package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Film;
import com.example.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // 根据用户ID推荐电影
    @GetMapping("/{userId}")
    public Result getFilmsByUser(@PathVariable Integer userId) {
        // 调用 service 层的方法获取推荐电影
        List<Film> films = recommendationService.recommendFilmsByUser(userId);

        // 判断推荐的电影是否为空
        if (films == null || films.isEmpty()) {
            return Result.error(ResultCodeEnum.DATA_ERROR.code, "没有找到推荐的电影");
        }

        // 返回成功的结果并传递推荐的电影列表
        return Result.success(films);
    }
}
