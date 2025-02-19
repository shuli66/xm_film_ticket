package com.example.mapper;

import com.example.entity.Actor;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ActorMapper {

    int insert(Actor actor);

    void updateById(Actor actor);

    void deleteById(Integer id);

    @Select("select * from `actor` where id = #{id}")
    Actor selectById(Integer id);

    List<Actor> selectAll(Actor actor);

}
