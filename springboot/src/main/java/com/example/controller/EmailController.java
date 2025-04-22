package com.example.controller;

import com.example.common.Result;
import com.example.common.config.PassToken;
import com.example.common.enums.ResultCodeEnum;
import com.example.utils.EmailUtils;
import com.example.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱服务控制器
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Resource
    private RedisUtils redisUtils;
    
    @Resource
    private EmailUtils emailUtils;

    /**
     * 发送邮箱验证码
     */
    @PassToken
    @PostMapping("/sendVerificationCode")
    public Result sendVerificationCode(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        // 记录请求来源的详细信息
        logger.info("收到发送验证码请求: 目标邮箱={}, 当前环境={}", 
                email, 
                System.getProperty("spring.profiles.active", "dev"));
            
        if (StringUtils.isEmpty(email)) {
            logger.warn("邮箱为空，拒绝请求");
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "邮箱不能为空");
        }
        
        // 验证邮箱格式
        if (!email.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$")) {
            logger.warn("邮箱格式不正确: {}", email);
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "邮箱格式不正确");
        }
        
        // 检查是否频繁发送
        String rateLimitKey = "email_rate_limit:" + email;
        if (redisUtils.hasKey(rateLimitKey)) {
            logger.warn("邮箱发送频率限制: {}", email);
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "发送过于频繁，请稍后再试");
        }
        
        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(1000000));
        
        try {
            logger.info("准备发送邮箱验证码: 目标邮箱={}, 验证码={}", email, code);
            
            // 发送邮件
            emailUtils.sendVerificationCodeEmail(email, code);
            
            // 将验证码存入Redis，设置过期时间为5分钟
            String redisKey = "email_code:" + email;
            redisUtils.set(redisKey, code, 5, TimeUnit.MINUTES);
            
            // 设置发送频率限制（60秒内不能重复发送）
            redisUtils.set(rateLimitKey, "1", 60, TimeUnit.SECONDS);
            
            logger.info("邮箱验证码发送成功: {}", email);
            return Result.success();
        } catch (Exception e) {
            logger.error("发送邮箱验证码失败: 目标邮箱={}, 错误消息={}", email, e.getMessage(), e);
            
            // 根据错误类型返回更友好的提示
            if (e.getMessage() != null && e.getMessage().contains("non-existent account")) {
                return Result.error(ResultCodeEnum.SERVICE_ERROR.code, "邮箱地址可能不存在，请检查后重试");
            }
            
            return Result.error(ResultCodeEnum.SERVICE_ERROR.code, "验证码发送失败，请稍后重试");
        }
    }
    
    /**
     * 验证邮箱验证码
     */
    @PassToken
    @PostMapping("/verifyCode")
    public Result verifyCode(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String code = data.get("code");
        
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)) {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "参数不能为空");
        }
        
        // 从Redis获取验证码
        String redisKey = "email_code:" + email;
        Object storedCode = redisUtils.get(redisKey);
        
        if (storedCode == null) {
            return Result.error(ResultCodeEnum.CODE_ERROR.code, "验证码已过期");
        }
        
        if (!code.equals(storedCode.toString())) {
            return Result.error(ResultCodeEnum.CODE_ERROR.code, "验证码错误");
        }
        
        // 验证成功后删除Redis中的验证码
        redisUtils.delete(redisKey);
        
        return Result.success();
    }
} 