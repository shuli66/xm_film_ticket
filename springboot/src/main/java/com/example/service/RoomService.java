package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Room;
import com.example.mapper.RoomMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 影厅房间业务层方法
 */
@Service
public class RoomService {

    @Resource
    private RoomMapper roomMapper;

    public void add(Room room) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.CINEMA.name().equals(currentUser.getRole())) {
            // 如果是影院管理员，使用当前用户ID作为cinemaId
            room.setCinemaId(currentUser.getId());
        }
        // 如果是系统管理员，使用前端传入的cinemaId
        roomMapper.insert(room);
    }

    public void updateById(Room room) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 只有在CINEMA角色且没有指定cinemaId时才设置影院ID
        if (RoleEnum.CINEMA.name().equals(currentUser.getRole()) && room.getCinemaId() == null) {
            room.setCinemaId(currentUser.getId());
        }
        // 保留前端传入的cinemaId
        roomMapper.updateById(room);
    }

    public void deleteById(Integer id) {
        roomMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            roomMapper.deleteById(id);
        }
    }

    public Room selectById(Integer id) {
        return roomMapper.selectById(id);
    }

    public List<Room> selectAll(Room room) {
        return roomMapper.selectAll(room);
    }

    public PageInfo<Room> selectPage(Room room, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.CINEMA.name().equals(currentUser.getRole())) {
            room.setCinemaId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Room> list = roomMapper.selectAll(room);
        return PageInfo.of(list);
    }

}
