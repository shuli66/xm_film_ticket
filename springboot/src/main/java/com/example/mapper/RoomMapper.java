package com.example.mapper;

import com.example.entity.Room;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoomMapper {

    int insert(Room room);

    void updateById(Room room);

    void deleteById(Integer id);

    @Select("select * from `room` where id = #{id}")
    Room selectById(Integer id);

    List<Room> selectAll(Room room);

}
