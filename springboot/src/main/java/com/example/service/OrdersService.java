package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.FilmMapper;
import com.example.mapper.FilmShowMapper;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 订单信息业务层方法
 */
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private FilmShowMapper filmShowMapper;
    @Resource
    private FilmMapper filmMapper;

    @Transactional
    public void add(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        orders.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        orders.setCreateTime(DateUtil.now());
        orders.setStatus("待支付");

        // 服务端座位冲突校验：查同场次所有未退票订单，取交集
        Orders query = new Orders();
        query.setShowId(orders.getShowId());
        List<Orders> existingOrders = ordersMapper.selectAll(query);
        Set<String> soldSeats = new HashSet<>();
        for (Orders existing : existingOrders) {
            if (!"已退票".equals(existing.getStatus())) {
                List<Seat> seats = JSONUtil.toList(existing.getSeat(), Seat.class);
                for (Seat seat : seats) {
                    soldSeats.add(seat.getRow() + "-" + seat.getCol());
                }
            }
        }
        for (Seat seat : orders.getSeatList()) {
            if (soldSeats.contains(seat.getRow() + "-" + seat.getCol())) {
                throw new CustomException("500", "座位 " + seat.getRow() + "排" + seat.getCol() + "列 已被占用，请重新选座");
            }
        }

        // 计算订单总价格
        FilmShow filmShow = filmShowMapper.selectById(orders.getShowId());
        orders.setPrice(filmShow.getPrice() * orders.getSeatList().size());
        orders.setSeat(JSONUtil.toJsonStr(orders.getSeatList()));

        ordersMapper.insert(orders);

    }

    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    public List<Orders> selectAll(Orders orders) {
        List<Orders> dbOrders = ordersMapper.selectAll(orders);
        for (Orders dbOrder : dbOrders) {
            dbOrder.setSeatList(JSONUtil.toList(dbOrder.getSeat(), Seat.class));
        }
        return dbOrders;
    }

    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            orders.setUserId(currentUser.getId());
        }
        if (RoleEnum.CINEMA.name().equals(currentUser.getRole())) {
            orders.setCinemaId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = selectAll(orders);
        return PageInfo.of(list);
    }

    @Transactional
    public void cancel(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            throw new CustomException("500", "订单不存在");
        }
        // 校验当前用户是否是订单主人
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && RoleEnum.USER.name().equals(currentUser.getRole())) {
            if (!orders.getUserId().equals(currentUser.getId())) {
                throw new CustomException("403", "无权操作他人订单");
            }
        }
        // 只有待支付或待取票状态才能退票
        if (!"待支付".equals(orders.getStatus()) && !"待取票".equals(orders.getStatus())) {
            throw new CustomException("500", "当前订单状态不允许退票");
        }
        // 时间校验：开场前1小时内不能退（毫秒比较，3600*1000=3600000）
        long time = DateUtil.parse(orders.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
        long now = DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss").getTime();
        if (now + 3600000L > time) {
            throw new CustomException("500", "您已错过退票时间，无法退票");
        }
        // 保存原状态，判断是否需要扣减票房（只有已支付的才有票房）
        String originalStatus = orders.getStatus();
        orders.setStatus("已退票");
        ordersMapper.updateById(orders);
        // 扣除电影的票房（仅已支付=待取票 的订单才扣）
        if ("待取票".equals(originalStatus)) {
            Film film = filmMapper.selectById(orders.getFilmId());
            if (ObjectUtil.isNotEmpty(film)) {
                film.setTotal(film.getTotal() - orders.getPrice());
                filmMapper.updateById(film);
            }
        }
    }

    public Map<String, Object> todayTotal() {
        Orders orders = new Orders();
        orders.setCreateTime(DateUtil.today());
        List<Orders> list = ordersMapper.selectAll(orders);
        // 只统计已支付的订单金额
        double sum = list.stream()
                .filter(x -> ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())) && x.getCreateTime().contains(DateUtil.today()))
                .mapToDouble(Orders::getPrice)
                .sum();

        Map<String, Object> map = new HashMap<>();
        map.put("total", sum);
        map.put("time", DateUtil.now());
        return map;
    }

    public Double selectTodayPrice(Integer filmId) {
        Orders orders = new Orders();
        orders.setFilmId(filmId);
        List<Orders> list = ordersMapper.selectAll(orders);
        return list.stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus()))
                        && x.getCreateTime().contains(DateUtil.today()))
                .mapToDouble(Orders::getPrice)
                .sum();
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    /**
     * 更新订单状态为已取票
     */
    public void pickup(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            throw new CustomException("500", "订单不存在");
        }
        if (!"待取票".equals(orders.getStatus())) {
            throw new CustomException("500", "订单状态不正确，无法取票");
        }
        orders.setStatus("已取票");
        ordersMapper.updateById(orders);
    }
}
