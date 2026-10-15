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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * JWT拦截器
 * 1. 校验 token 合法性与有效期
 * 2. 按「路径 + 请求方法」做角色权限校验（集中式，避免每个 Controller 重复写）
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

    /**
     * 写操作（POST/PUT/DELETE）的角色限制规则。
     * key = 路径正则，value = 允许的角色集合；按声明顺序匹配，命中即校验。
     * 未命中任何规则的写操作：只要求登录（对象级归属校验在 Service 层完成）。
     */
    private static final Map<Pattern, String[]> WRITE_ROLE_RULES = new LinkedHashMap<>();

    /**
     * 读操作（GET）中需要限制角色的路径规则。
     */
    private static final Map<Pattern, String[]> READ_ROLE_RULES = new LinkedHashMap<>();

    static {
        // ---- 写操作规则 ----
        // 管理员专属资源
        WRITE_ROLE_RULES.put(Pattern.compile("^/admin(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});
        WRITE_ROLE_RULES.put(Pattern.compile("^/(actor|area|type|video|notice)(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});
        // 电影/影厅/场次：管理员与影院都可维护
        WRITE_ROLE_RULES.put(Pattern.compile("^/film(/.*)?$"), new String[]{RoleEnum.ADMIN.name(), RoleEnum.CINEMA.name()});
        WRITE_ROLE_RULES.put(Pattern.compile("^/(room|show)(/.*)?$"), new String[]{RoleEnum.ADMIN.name(), RoleEnum.CINEMA.name()});
        // 影院信息的增删改：仅管理员（影院改自己资料由 Controller 内部再校验）
        WRITE_ROLE_RULES.put(Pattern.compile("^/cinema(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});
        // 用户管理的增删：仅管理员（/user/update 允许本人，走 Service 层归属校验）
        WRITE_ROLE_RULES.put(Pattern.compile("^/user/(add|delete)(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});
        // 统计报表：管理员与影院
        WRITE_ROLE_RULES.put(Pattern.compile("^/statistics(/.*)?$"), new String[]{RoleEnum.ADMIN.name(), RoleEnum.CINEMA.name()});
        // 删除评分：仅管理员
        WRITE_ROLE_RULES.put(Pattern.compile("^/score/delete(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});

        // ---- 读操作规则 ----
        // 管理员信息列表/详情：仅管理员（防止明文密码泄露）
        READ_ROLE_RULES.put(Pattern.compile("^/admin(/.*)?$"), new String[]{RoleEnum.ADMIN.name()});
        // 用户列表/分页：仅管理员
        READ_ROLE_RULES.put(Pattern.compile("^/user/(selectAll|selectPage)$"), new String[]{RoleEnum.ADMIN.name()});
        // 统计报表：管理员与影院
        READ_ROLE_RULES.put(Pattern.compile("^/statistics(/.*)?$"), new String[]{RoleEnum.ADMIN.name(), RoleEnum.CINEMA.name()});
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        logger.debug("请求路径: {}", requestURI);

        // 检查是否有PassToken注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            PassToken passToken = handlerMethod.getMethodAnnotation(PassToken.class);
            if (passToken == null) {
                passToken = handlerMethod.getBeanType().getAnnotation(PassToken.class);
            }
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
            "/email/captcha",               // 图片验证码
            "/email/verifyCaptcha",         // 验证图片验证码
            "/alipay/notify",
            "/alipay/pay",
            "/files"
        };

        for (String path : whiteList) {
            if (requestURI.equals(path) || (path.endsWith("/*") && requestURI.startsWith(path.substring(0, path.length() - 1)))) {
                logger.debug("接口在白名单中，跳过token验证: {}", requestURI);
                return true;
            }
        }

        // 1. 从http请求标头里面拿到token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            token = request.getParameter(Constants.TOKEN);
        }

        // 2. 开始执行认证
        if (ObjectUtil.isNull(token)) {
            logger.error("请求无Token: {}", requestURI);
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        String role = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            role = audience.split("-")[1];
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
        if (ObjectUtil.isNull(account)) {
            logger.error("Token中的用户不存在");
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        try {
            // 通过用户的密码作为密钥再次验证token的合法性
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            logger.error("Token验证失败: {}", e.getMessage());
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        // 3. 角色权限校验
        checkRole(request.getMethod(), requestURI, role);

        return true;
    }

    /**
     * 根据请求方法与路径校验当前角色是否有权限访问
     */
    private void checkRole(String method, String requestURI, String role) {
        Map<Pattern, String[]> rules;
        if ("GET".equalsIgnoreCase(method)) {
            rules = READ_ROLE_RULES;
        } else if ("POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method)) {
            rules = WRITE_ROLE_RULES;
        } else {
            return;
        }

        for (Map.Entry<Pattern, String[]> entry : rules.entrySet()) {
            if (entry.getKey().matcher(requestURI).matches()) {
                String[] allowed = entry.getValue();
                if (role == null || !Arrays.asList(allowed).contains(role)) {
                    logger.warn("权限不足，拒绝访问。路径: {} 方法: {} 角色: {} 允许角色: {}",
                            requestURI, method, role, Arrays.toString(allowed));
                    throw new CustomException(ResultCodeEnum.PERMISSION_ERROR);
                }
                return;
            }
        }
    }

}
