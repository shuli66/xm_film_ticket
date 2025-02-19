package com.example.controller;

import com.example.common.Result;
import com.example.entity.Cinema;
import com.example.service.CinemaService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电影院信息前端请求接口
 */
@RestController
@RequestMapping("/cinema")
public class CinemaController {

    @Resource
    private CinemaService cinemaService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Cinema cinema) {
        cinemaService.add(cinema);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Cinema cinema) {
        cinemaService.updateById(cinema);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        cinemaService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        cinemaService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Cinema cinema = cinemaService.selectById(id);
        return Result.success(cinema);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Cinema cinema) {
        List<Cinema> list = cinemaService.selectAll(cinema);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Cinema cinema,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Cinema> pageInfo = cinemaService.selectPage(cinema, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
