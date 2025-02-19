package com.example.mapper;

import com.example.entity.Cinema;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CinemaMapper {

    int insert(Cinema cinema);

    void updateById(Cinema cinema);

    void deleteById(Integer id);

    @Select("select * from `cinema` where id = #{id}")
    Cinema selectById(Integer id);

    @Select("select * from `cinema` where username = #{username}")
    Cinema selectByUsername(String username);

    List<Cinema> selectAll(Cinema cinema);

}
