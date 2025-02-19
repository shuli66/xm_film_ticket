package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Video;
import com.example.mapper.VideoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电影预告片业务层方法
 */
@Service
public class VideoService {

    @Resource
    private VideoMapper videoMapper;

    public void add(Video video) {
        video.setTime(DateUtil.now());
        videoMapper.insert(video);
    }

    public void updateById(Video video) {
        videoMapper.updateById(video);
    }

    public void deleteById(Integer id) {
        videoMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            videoMapper.deleteById(id);
        }
    }

    public Video selectById(Integer id) {
        return videoMapper.selectById(id);
    }

    public List<Video> selectAll(Video video) {
        return videoMapper.selectAll(video);
    }

    public PageInfo<Video> selectPage(Video video, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Video> list = videoMapper.selectAll(video);
        return PageInfo.of(list);
    }

}
