package com.example.service;

import com.example.entity.Actor;
import com.example.mapper.ActorMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 演职人员业务层方法
 */
@Service
public class ActorService {

    @Resource
    private ActorMapper actorMapper;

    public void add(Actor actor) {
        actorMapper.insert(actor);
    }

    public void updateById(Actor actor) {
        actorMapper.updateById(actor);
    }

    public void deleteById(Integer id) {
        actorMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            actorMapper.deleteById(id);
        }
    }

    public Actor selectById(Integer id) {
        return actorMapper.selectById(id);
    }

    public List<Actor> selectAll(Actor actor) {
        return actorMapper.selectAll(actor);
    }

    public PageInfo<Actor> selectPage(Actor actor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Actor> list = actorMapper.selectAll(actor);
        return PageInfo.of(list);
    }

}
