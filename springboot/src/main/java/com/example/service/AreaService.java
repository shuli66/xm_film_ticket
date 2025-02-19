package com.example.service;

import com.example.entity.Area;
import com.example.mapper.AreaMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电影区域类业务层方法
 */
@Service
public class AreaService {

    @Resource
    private AreaMapper areaMapper;

    public void add(Area area) {
        areaMapper.insert(area);
    }

    public void updateById(Area area) {
        areaMapper.updateById(area);
    }

    public void deleteById(Integer id) {
        areaMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            areaMapper.deleteById(id);
        }
    }

    public Area selectById(Integer id) {
        return areaMapper.selectById(id);
    }

    public List<Area> selectAll(Area area) {
        return areaMapper.selectAll(area);
    }

    public PageInfo<Area> selectPage(Area area, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Area> list = areaMapper.selectAll(area);
        return PageInfo.of(list);
    }

}
