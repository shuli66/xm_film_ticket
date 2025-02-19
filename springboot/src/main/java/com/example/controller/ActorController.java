package com.example.controller;

import com.example.common.Result;
import com.example.entity.Actor;
import com.example.service.ActorService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 演职人员前端请求接口
 */
@RestController
@RequestMapping("/actor")
public class ActorController {

    @Resource
    private ActorService actorService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Actor actor) {
        actorService.add(actor);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Actor actor) {
        actorService.updateById(actor);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        actorService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        actorService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Actor actor = actorService.selectById(id);
        return Result.success(actor);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Actor actor) {
        List<Actor> list = actorService.selectAll(actor);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Actor actor,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Actor> pageInfo = actorService.selectPage(actor, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
