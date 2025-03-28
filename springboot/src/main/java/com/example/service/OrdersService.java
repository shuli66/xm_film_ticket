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
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.el.parser.Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserMapper userMapper;
    @Resource
    private FilmMapper filmMapper;

    @Transactional
    public void add(Orders orders) {
        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());
        orders.setOrderNo(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        orders.setCreateTime(DateUtil.now());
        orders.setStatus("待支付");

        // 计算订单总价格
        FilmShow filmShow = filmShowMapper.selectById(orders.getShowId());
        orders.setPrice(filmShow.getPrice() * orders.getSeatList().size());
        orders.setSeat(JSONUtil.toJsonStr(orders.getSeatList()));

        User user = userMapper.selectById(currentUser.getId());

        userMapper.updateById(user);
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

    public void cancel(Integer id) {
        Orders orders = ordersMapper.selectById(id);
        long time = DateUtil.parse(orders.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
        long now = DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm:ss").getTime();
        if (now + 3600 > time) {
            throw new CustomException("500", "您已错过退票时间，无法退票");
        }
        orders.setStatus("已退票");
        ordersMapper.updateById(orders);
//        // 返回用户的钱
//        User user = userMapper.selectById(orders.getUserId());
//        user.setAccount(user.getAccount() + orders.getPrice());
//        userMapper.updateById(user);
        // 扣除电影的票房
        Film film = filmMapper.selectById(orders.getFilmId());
        if (ObjectUtil.isNotEmpty(film)) {
            film.setTotal(film.getTotal() - orders.getPrice());
            filmMapper.updateById(film);
        }
    }

    public Map<String, Object> todayTotal() {
        Orders orders = new Orders();
        orders.setCreateTime(DateUtil.today());
        List<Orders> list = ordersMapper.selectAll(orders);
        double sum = list.stream().mapToDouble(Orders::getPrice).sum();

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
                .filter(x -> !"已退票".equals(x.getStatus()) && x.getCreateTime().contains(DateUtil.today()))
                .mapToDouble(Orders::getPrice)
                .sum();
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }
}
