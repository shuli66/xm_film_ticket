package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.CinemaService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * JWT拦截器
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JWTInterceptor.class);

    @Resource
    private AdminService adminService;
    @Resource
    private CinemaService cinemaService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        logger.debug("请求路径: {}", requestURI);
        
        // 检查是否有PassToken注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的PassToken注解
            PassToken passToken = handlerMethod.getMethodAnnotation(PassToken.class);
            // 如果方法上没有，则检查类上是否有PassToken注解
            if (passToken == null) {
                passToken = handlerMethod.getBeanType().getAnnotation(PassToken.class);
            }
            // 如果有PassToken注解，且required=true，则跳过验证
            if (passToken != null && passToken.required()) {
                logger.debug("接口标记了PassToken注解，跳过token验证: {}", requestURI);
                return true;
            }
        }
        
        // 定义白名单路径
        String[] whiteList = {
            "/login",
            "/register",
            "/user/resetPassword",
            "/user/checkUsername",
            "/user/checkPhone",
            "/user/sendCode",
            "/map",
            "/email/sendVerificationCode",
            "/email/verifyCode",
            "/alipay/notify",
            "/alipay/pay",
            "/files"
        };
        
        // 检查是否在白名单中
        for (String path : whiteList) {
            if (requestURI.equals(path) || (path.endsWith("/*") && requestURI.startsWith(path.substring(0, path.length() - 1)))) {
                logger.debug("接口在白名单中，跳过token验证: {}", requestURI);
                return true;
            }
        }

        // 1. 从http请求标头里面拿到token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            // 如果没拿到，那么再从请求参数里拿一次
            token = request.getParameter(Constants.TOKEN);
        }


        // 2. 开始执行认证
        if (ObjectUtil.isNull(token)) {
            logger.error("请求无Token: {}", requestURI);
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            String role = audience.split("-")[1];
            // 根据用户角色判断用户属于哪个数据库表 然后查询用户数据
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            }
            if (RoleEnum.CINEMA.name().equals(role)) {
                account = cinemaService.selectById(Integer.valueOf(userId));
            }
            if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            logger.error("Token解析错误: {}", e.getMessage());
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 根据token里面携带的用户ID去对应的角色表查询  没查到 所有报了这个"用户不存在"错误
        if (ObjectUtil.isNull(account)) {
            // 用户不存在
            logger.error("Token中的用户不存在");
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        try {
            // 通过用户的密码作为密钥再次验证token的合法性
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);  // 验证token
        } catch (JWTVerificationException e) {
            // 用户不存在
            logger.error("Token验证失败: {}", e.getMessage());
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        return true;
    }

}
