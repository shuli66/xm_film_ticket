package com.example.controller;

import com.example.common.Result;
import com.example.entity.Comment;
import com.example.entity.Film;
import com.example.service.CommentService;
import com.example.service.FilmService;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电影信息前端请求接口
 */
@RestController
@RequestMapping("/film")
public class FilmController {

    @Resource
    private FilmService filmService;



    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Film film) {
        filmService.add(film);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Film film) {
        filmService.updateById(film);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        filmService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        filmService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Film film = filmService.selectById(id);
        return Result.success(film);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Film film) {
        List<Film> list = filmService.selectAll(film);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Film film,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Film> pageInfo = filmService.selectPage(film, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 电影总票房Top10
     */
    @GetMapping("/selectTotalTop")
    public Result selectTotalTop10() {
        List<Film> list = filmService.selectTotalTop10();
        return Result.success(list);
    }

    /**
     * 电影评分Top10
     */
    @GetMapping("/selectScoreTop")
    public Result selectScoreTop10() {
        List<Film> list = filmService.selectScoreTop10();
        return Result.success(list);
    }

    @GetMapping("/selectPriceRanking/{filmId}")
    public Result selectPriceRanking(@PathVariable Integer filmId) {
        Integer ranking = filmService.selectPriceRanking(filmId);
        return Result.success(ranking);
    }



}
