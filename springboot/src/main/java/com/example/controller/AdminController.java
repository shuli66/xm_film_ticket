package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员信息前端请求接口（仅限 ADMIN 角色访问）
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 校验当前用户是否为 ADMIN
     */
    private boolean isAdmin() {
        Account currentUser = TokenUtils.getCurrentUser();
        return currentUser != null && RoleEnum.ADMIN.name().equals(currentUser.getRole());
    }

    /**
     * 新增管理员（仅 ADMIN）
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        adminService.add(admin);
        return Result.success();
    }

    /**
     * 修改（仅 ADMIN）
     */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        adminService.updateById(admin);
        return Result.success();
    }

    /**
     * 单个删除（仅 ADMIN）
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        adminService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除（仅 ADMIN）
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        adminService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询（仅 ADMIN）
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /**
     * 查询所有（仅 ADMIN）
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 分页查询（仅 ADMIN）
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        if (!isAdmin()) {
            return Result.error("403", "无权限，仅管理员可执行此操作");
        }
        PageInfo<Admin> pageInfo = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
