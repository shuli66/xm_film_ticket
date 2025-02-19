package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.Collect;
import com.example.entity.Film;
import com.example.mapper.CollectMapper;
import com.example.mapper.FilmMapper;
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
        List<Collect> collects = collectMapper.selectAll(collect);
        if (CollectionUtil.isNotEmpty(collects)) {
            deleteById(collects.get(0).getId());
        } else {
            collectMapper.insert(collect);
        }
    }

    public void deleteById(Integer id) {
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
