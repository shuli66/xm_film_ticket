package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Film;
import com.example.exception.CustomException;
import com.example.mapper.CollectMapper;
import com.example.mapper.FilmMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 想看业务层方法
 */
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private FilmMapper filmMapper;

    public void add(Collect collect) {
        // 用 Token 覆盖 userId，不信任前端传值
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null) {
            collect.setUserId(currentUser.getId());
        }
        List<Collect> collects = collectMapper.selectAll(collect);
        if (CollectionUtil.isNotEmpty(collects)) {
            deleteById(collects.get(0).getId());
        } else {
            collectMapper.insert(collect);
        }
    }

    public void deleteById(Integer id) {
        // 校验归属：用户只能删自己的收藏
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.USER.name().equals(currentUser.getRole())) {
            Collect collect = collectMapper.selectById(id);
            if (collect == null) {
                throw new CustomException("500", "收藏不存在");
            }
            if (!collect.getUserId().equals(currentUser.getId())) {
                throw new CustomException("403", "无权删除他人收藏");
            }
        }
        collectMapper.deleteById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        for (Collect dbCollect : list) {
            Film film = filmMapper.selectById(dbCollect.getFilmId());
            dbCollect.setFilm(film);
        }
        return PageInfo.of(list);
    }

}
