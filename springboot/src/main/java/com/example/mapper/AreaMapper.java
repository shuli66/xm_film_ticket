package com.example.mapper;

import com.example.entity.Area;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AreaMapper {

    int insert(Area area);

    void updateById(Area area);

    void deleteById(Integer id);

    @Select("select * from `area` where id = #{id}")
    Area selectById(Integer id);

    List<Area> selectAll(Area area);

}
