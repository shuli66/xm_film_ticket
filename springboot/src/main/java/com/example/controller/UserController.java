package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 用户信息前端请求接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @PostMapping("/recharge")
    public Result recharge(@RequestBody User user) {
        User dbUser = userService.recharge(user);
        return Result.success(dbUser);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        userService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> pageInfo = userService.selectPage(user, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @PostMapping("/sendCode")
    public Result sendCode(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");
        if (StringUtils.isEmpty(phone)) {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "手机号不能为空");
        }
        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        // TODO: 这里应该调用短信服务发送验证码，这里先模拟发送
        // 实际项目中应该将验证码存入Redis，并设置过期时间
        return Result.success(code);
    }

    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");
        String newPassword = data.get("newPassword");
        
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(newPassword)) {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "参数不能为空");
        }
        
        // 根据手机号查找用户
        User user = userService.selectByPhone(phone);
        if (user == null) {
            return Result.error(ResultCodeEnum.DATA_ERROR.code, "用户不存在");
        }
        
        // 更新密码
        user.setPassword(newPassword);
        userService.updateById(user);
        
        return Result.success();
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/checkUsername")
    public Result checkUsername(@RequestParam String username) {
        User user = userService.selectByUsername(username);
        return Result.success(user != null);
    }

}
