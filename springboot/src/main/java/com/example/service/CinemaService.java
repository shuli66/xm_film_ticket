package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Cinema;
import com.example.exception.CustomException;
import com.example.mapper.CinemaMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电影院信息业务层方法
 */
@Service
public class CinemaService {

    @Resource
    private CinemaMapper cinemaMapper;

    public void add(Cinema cinema) {
        Cinema dbCinema = cinemaMapper.selectByUsername(cinema.getUsername());
        if (ObjectUtil.isNotNull(dbCinema)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(cinema.getPassword())) {
            cinema.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(cinema.getName())) {
            cinema.setName(cinema.getUsername());
        }
        cinema.setRole(RoleEnum.CINEMA.name());
        cinema.setStatus("待审核");
        cinemaMapper.insert(cinema);
    }

    public void updateById(Cinema cinema) {
        cinemaMapper.updateById(cinema);
    }

    public void deleteById(Integer id) {
        cinemaMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            cinemaMapper.deleteById(id);
        }
    }

    public Cinema selectById(Integer id) {
        return cinemaMapper.selectById(id);
    }

    public List<Cinema> selectAll(Cinema cinema) {
        return cinemaMapper.selectAll(cinema);
    }

    public PageInfo<Cinema> selectPage(Cinema cinema, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cinema> list = cinemaMapper.selectAll(cinema);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Cinema login(Account account) {
        Cinema dbCinema = cinemaMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbCinema)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbCinema.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbCinema.getId() + "-" + dbCinema.getRole(), dbCinema.getPassword());
        dbCinema.setToken(token);
        return dbCinema;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Cinema dbCinema = cinemaMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbCinema)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbCinema.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbCinema.setPassword(account.getNewPassword());
        cinemaMapper.updateById(dbCinema);
    }

    public void register(Account account) {
        Cinema cinema = new Cinema();
        BeanUtils.copyProperties(account, cinema);
        add(cinema);
    }
}
