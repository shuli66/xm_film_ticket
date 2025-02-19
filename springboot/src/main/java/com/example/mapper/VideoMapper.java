package com.example.mapper;

import com.example.entity.Video;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface VideoMapper {

    int insert(Video video);

    void updateById(Video video);

    void deleteById(Integer id);

    @Select("select * from `video` where id = #{id}")
    Video selectById(Integer id);

    List<Video> selectAll(Video video);

}
