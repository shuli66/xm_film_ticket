package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Cinema;
import com.example.service.CinemaService;
import com.example.utils.TokenUtils;
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

    private Account currentUser() {
        return TokenUtils.getCurrentUser();
    }

    private boolean isAdmin() {
        Account u = currentUser();
        return u != null && RoleEnum.ADMIN.name().equals(u.getRole());
    }

    private boolean isCinema() {
        Account u = currentUser();
        return u != null && RoleEnum.CINEMA.name().equals(u.getRole());
    }

    /**
     * 新增影院（仅 ADMIN）
     */
    @PostMapping("/add")
    public Result add(@RequestBody Cinema cinema) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可新增影院");
        }
        cinemaService.add(cinema);
        return Result.success();
    }

    /**
     * 修改影院信息
     * ADMIN：可修改任意影院（含审核）
     * CINEMA：只能修改自己的影院，且不能自行改审核状态
     */
    @PutMapping("/update")
    public Result update(@RequestBody Cinema cinema) {
        Account u = currentUser();
        if (u == null) {
            return Result.error("401", "请先登录");
        }
        if (isCinema()) {
            // 影院只能改自己的资料
            if (cinema.getId() == null || !cinema.getId().equals(u.getId())) {
                return Result.error("403", "无权修改其他影院的信息");
            }
            // 防止影院自行通过审核：审核状态字段不允许由影院自己修改
            Cinema db = cinemaService.selectById(u.getId());
            cinema.setStatus(db == null ? null : db.getStatus());
        } else if (!isAdmin()) {
            return Result.error("403", "无权限执行此操作");
        }
        cinemaService.updateById(cinema);
        return Result.success();
    }

    /**
     * 单个删除（仅 ADMIN）
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可删除影院");
        }
        cinemaService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除（仅 ADMIN）
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可删除影院");
        }
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
