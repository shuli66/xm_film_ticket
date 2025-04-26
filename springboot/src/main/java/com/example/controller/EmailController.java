package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.Result;
import com.example.common.config.PassToken;
import com.example.common.enums.ResultCodeEnum;
import com.example.utils.EmailUtils;
import com.example.utils.RedisUtils;
import com.google.code.kaptcha.Producer;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱服务控制器
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    
    // JWT验证码密钥
    private static final String CAPTCHA_SECRET = "YourCaptchaSecretKey123!@#";
    // JWT验证码有效期（毫秒）- 5分钟
    private static final long CAPTCHA_EXPIRATION = 5 * 60 * 1000;

    @Resource
    private RedisUtils redisUtils;
    
    @Resource
    private EmailUtils emailUtils;
    
    @Resource
    private Producer kaptchaProducer;

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
    
    /**
     * 生成图片验证码 - 使用JWT方式
     */
    @PassToken
    @GetMapping("/captcha")
    public Result getCaptcha() {
        try {
            // 生成验证码文本
            String captchaText = kaptchaProducer.createText();
            
            // 创建JWT token，包含验证码和过期时间
            String token = createCaptchaToken(captchaText);
            
            // 生成验证码图片的Base64编码
            BufferedImage image = kaptchaProducer.createImage(captchaText);
            String base64Image = "";
            
            try {
                java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
                ImageIO.write(image, "jpg", outputStream);
                base64Image = java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());
            } catch (IOException e) {
                logger.error("生成验证码图片失败", e);
                return Result.error(ResultCodeEnum.SERVICE_ERROR.code, "生成验证码失败");
            }
            
            // 返回JWT token和Base64图片
            Map<String, String> responseData = new HashMap<>();
            responseData.put("captchaToken", token);
            responseData.put("captchaImage", "data:image/jpg;base64," + base64Image);
            
            return Result.success(responseData);
        } catch (Exception e) {
            logger.error("生成验证码失败", e);
            return Result.error(ResultCodeEnum.SERVICE_ERROR.code, "生成验证码失败");
        }
    }
    
    /**
     * 创建验证码JWT token
     */
    private String createCaptchaToken(String captchaText) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + CAPTCHA_EXPIRATION);
        
        return JWT.create()
                .withClaim("captcha", captchaText)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256(CAPTCHA_SECRET));
    }
    
    /**
     * 验证图片验证码 - 使用JWT方式
     */
    @PassToken
    @PostMapping("/verifyCaptcha")
    public Result verifyCaptcha(@RequestBody Map<String, String> data) {
        String captchaToken = data.get("captchaToken");
        String captchaCode = data.get("captchaCode");
        
        if (StringUtils.isEmpty(captchaToken) || StringUtils.isEmpty(captchaCode)) {
            return Result.error(ResultCodeEnum.PARAM_ERROR.code, "参数不能为空");
        }
        
        try {
            // 验证JWT token
            Algorithm algorithm = Algorithm.HMAC256(CAPTCHA_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(captchaToken);
            
            // 获取JWT中存储的验证码
            String storedCaptcha = jwt.getClaim("captcha").asString();
            
            // 验证码比对（不区分大小写）
            if (!captchaCode.equalsIgnoreCase(storedCaptcha)) {
                return Result.error(ResultCodeEnum.CODE_ERROR.code, "验证码错误");
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("验证码验证失败", e);
            return Result.error(ResultCodeEnum.CODE_ERROR.code, "验证码已过期或无效");
        }
    }
} 