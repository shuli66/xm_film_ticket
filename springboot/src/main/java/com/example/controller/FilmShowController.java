package com.example.controller;

import com.example.common.Result;
import com.example.entity.Cinema;
import com.example.entity.Film;
import com.example.entity.FilmShow;
import com.example.service.FilmShowService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 放映记录前端请求接口
 */
@RestController
@RequestMapping("/show")
public class FilmShowController {

    @Resource
    private FilmShowService filmShowService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody FilmShow filmShow) {
        filmShowService.add(filmShow);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody FilmShow filmShow) {
        filmShowService.updateById(filmShow);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        filmShowService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        filmShowService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        FilmShow filmShow = filmShowService.selectById(id);
        return Result.success(filmShow);
    }

    @GetMapping("/selectByFilmId/{id}")
    public Result selectByFilmId(@PathVariable Integer id) {
        List<Cinema> list = filmShowService.selectByFilmId(id);
        return Result.success(list);
    }

    @GetMapping("/selectByCinemaId/{id}")
    public Result selectByCinemaId(@PathVariable Integer id) {
        List<Film> list = filmShowService.selectByCinemaId(id);
        return Result.success(list);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(FilmShow filmShow) {
        List<FilmShow> list = filmShowService.selectAll(filmShow);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(FilmShow filmShow,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<FilmShow> pageInfo = filmShowService.selectPage(filmShow, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
