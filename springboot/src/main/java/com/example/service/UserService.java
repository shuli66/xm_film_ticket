package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Score;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.ScoreMapper;
import com.example.mapper.UserMapper;
import com.example.utils.RedisUtils;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户信息业务层方法
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    // JWT验证码密钥 - 需要与EmailController中的密钥保持一致
    private static final String CAPTCHA_SECRET = "YourCaptchaSecretKey123!@#";

    @Resource
    private UserMapper userMapper;

    @Resource
    private ScoreMapper scoreMapper;

    @Resource
    private RedisUtils redisUtils;

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    public User selectById(Integer id) {
        User dbUser = userMapper.selectById(id);
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User selectByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }



    /**
     * 登录
     */
    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

    public Account register(Account account) {
        // 检查用户名是否已存在
        User user = userMapper.selectByUsername(account.getUsername());
        if (user != null) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        
        String verificationCode = account.getVerificationCode();
        String verificationType = account.getVerificationType();
        
        if (StringUtils.isEmpty(verificationCode)) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        
        // 根据验证类型执行不同的验证逻辑
        if ("EMAIL".equals(verificationType)) {
            // 邮箱验证码验证
            String email = account.getEmail();
            if (StringUtils.isEmpty(email)) {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        
        // 从Redis中获取验证码
        String redisKey = "email_code:" + email;
        Object storedCode = redisUtils.get(redisKey);
        
        if (storedCode == null) {
            throw new CustomException(ResultCodeEnum.CODE_ERROR);
        }
        
        if (!verificationCode.equals(storedCode.toString())) {
            throw new CustomException(ResultCodeEnum.CODE_ERROR);
        }
        
        // 验证成功后删除Redis中的验证码
        redisUtils.delete(redisKey);
        } else if ("CAPTCHA".equals(verificationType)) {
            // 图片验证码验证 - 使用JWT方式
            String captchaToken = account.getCaptchaToken();
            if (StringUtils.isEmpty(captchaToken)) {
                throw new CustomException(ResultCodeEnum.PARAM_ERROR);
            }
            
            try {
                // 验证JWT token
                Algorithm algorithm = Algorithm.HMAC256(CAPTCHA_SECRET);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(captchaToken);
                
                // 获取JWT中存储的验证码
                String storedCaptcha = jwt.getClaim("captcha").asString();
                
                // 验证码比对（不区分大小写）
                if (!verificationCode.equalsIgnoreCase(storedCaptcha)) {
                    throw new CustomException(ResultCodeEnum.CODE_ERROR);
                }
            } catch (Exception e) {
                logger.error("验证码验证失败: {}", e.getMessage());
                throw new CustomException(ResultCodeEnum.CODE_ERROR);
            }
        } else {
            throw new CustomException(ResultCodeEnum.PARAM_ERROR);
        }
        
        // 创建新用户
        User newUser = new User();
        newUser.setUsername(account.getUsername());
        newUser.setPassword(account.getPassword());
        newUser.setRole(account.getRole());
        newUser.setPhone(account.getPhone());
        newUser.setEmail(account.getEmail());
        
        // 设置姓名/昵称
        if (account.getName() != null && !account.getName().isEmpty()) {
            newUser.setName(account.getName());
        } else {
            // 如果前端没有传递name，才使用username作为默认值
            newUser.setName(account.getUsername());
        }
        
        newUser.setAvatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
        
        // 保存用户
        userMapper.insert(newUser);
        
        // 返回用户信息
        account.setId(newUser.getId());
        return account;
    }

    public User recharge(User user) {
        Double rechargeNum = user.getAccount();
        User dbUser = userMapper.selectById(user.getId());
        dbUser.setAccount(dbUser.getAccount() + rechargeNum);
        userMapper.updateById(dbUser);
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<Score> getAllScores() {
        return scoreMapper.getAllScores();
    }

    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
