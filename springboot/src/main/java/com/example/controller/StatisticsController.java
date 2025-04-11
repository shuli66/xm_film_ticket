package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.Result;
import com.example.entity.Cinema;
import com.example.entity.Film;
import com.example.entity.Orders;
import com.example.entity.Type;
import com.example.mapper.FilmShowMapper;
import com.example.service.CinemaService;
import com.example.service.FilmService;
import com.example.service.OrdersService;
import com.example.service.TypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private FilmService filmService;
    @Resource
    private CinemaService cinemaService;
    @Resource
    private OrdersService ordersService;
    @Resource
    private TypeService typeService;

    @GetMapping("/base")
    public Result base() {
        Map<String, Object> map = new HashMap<>();

        List<Orders> orders = ordersService.selectAll(new Orders());
        double todayPrice = orders.stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus()))
                        && x.getCreateTime().contains(DateUtil.today()))
                .mapToDouble(Orders::getPrice)
                .sum();

        double totalPrice = orders.stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())))
                .mapToDouble(Orders::getPrice)
                .sum();

        map.put("filmNum", filmService.selectAll(new Film()).size());
        map.put("cinemaNum", cinemaService.selectAll(new Cinema()).size());
        map.put("todayPrice", todayPrice);
        map.put("totalPrice", totalPrice);
        return Result.success(map);
    }

    @GetMapping("/cinemaBase")
    public Result cinemaBase(@RequestParam Integer cinemaId) {
        Map<String, Object> map = new HashMap<>();

        Orders orderQuery = new Orders();
        orderQuery.setCinemaId(cinemaId);
        List<Orders> orders = ordersService.selectAll(orderQuery);
        
        double todayPrice = orders.stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus()))
                        && x.getCreateTime().contains(DateUtil.today()))
                .mapToDouble(Orders::getPrice)
                .sum();

        double totalPrice = orders.stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())))
                .mapToDouble(Orders::getPrice)
                .sum();

        map.put("todayPrice", todayPrice);
        map.put("totalPrice", totalPrice);
        return Result.success(map);
    }

    @GetMapping("/line")
    public Result line() {
        List<Double> yList = new ArrayList<>();

        // 获取最近7天的数据（年-月-日）放在xList里
        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -7);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();

        // 获取上述天对应的票房放在yList
        List<Orders> orders = ordersService.selectAll(new Orders());
        for (String day : xList) {
            double sum = orders.stream()
                    .filter(x -> !"已退票".equals(x.getStatus()) 
                            && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())) 
                            && x.getCreateTime().contains(day))
                    .mapToDouble(Orders::getPrice)
                    .sum();
            yList.add(sum);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("xAxis", xList);
        map.put("yAxis", yList);
        return Result.success(map);
    }

    /**
     * 根据影院ID获取该影院的票房趋势
     */
    @GetMapping("/cinemaLine")
    public Result cinemaLine(@RequestParam Integer cinemaId) {
        List<Double> yList = new ArrayList<>();

        // 获取最近7天的数据（年-月-日）放在xList里
        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -7);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();

        // 获取该影院上述天对应的票房放在yList
        Orders orderQuery = new Orders();
        orderQuery.setCinemaId(cinemaId);
        List<Orders> orders = ordersService.selectAll(orderQuery);
        for (String day : xList) {
            double sum = orders.stream()
                    .filter(x -> !"已退票".equals(x.getStatus()) 
                            && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())) 
                            && x.getCreateTime().contains(day))
                    .mapToDouble(Orders::getPrice)
                    .sum();
            yList.add(sum);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("xAxis", xList);
        map.put("yAxis", yList);
        return Result.success(map);
    }

    @GetMapping("/bar")
    public Result bar() {
        List<String> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();

        List<Type> types = typeService.selectAll(new Type());
        List<Orders> orders = ordersService.selectAll(new Orders())
                .stream()
                .filter(x -> !"已退票".equals(x.getStatus()) 
                        && ("已取票".equals(x.getStatus()) || "待取票".equals(x.getStatus())))
                .collect(Collectors.toList());
                
        for (Type type : types) {
            xList.add(type.getTitle());
            Double total = 0D;
            for (Orders order : orders) {
                Film film = filmService.selectById(order.getFilmId());
                if (ObjectUtil.isNotEmpty(film)) {
                    String typeIds = film.getTypeIds(); // [1,2]
                    List<Integer> typeIdList = JSONUtil.toList(typeIds, Integer.class);
                    if (typeIdList.contains(type.getId())) {
                        total += order.getPrice();
                    }
                }
            }
            yList.add(total);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("xAxis", xList);
        map.put("yAxis", yList);
        return Result.success(map);
    }

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> list = new ArrayList<>();

        List<Type> types = typeService.selectAll(new Type());
        List<Film> films = filmService.selectAll(new Film());
        for (Type type : types) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", type.getTitle());
            Integer num = 0;
            for (Film film : films) {
                String typeIds = film.getTypeIds(); // [1,2]
                List<Integer> typeIdList = JSONUtil.toList(typeIds, Integer.class);
                if (typeIdList.contains(type.getId())) {
                    num ++;
                }
            }
            map.put("value", num);
            list.add(map);
        }

        return Result.success(list);
    }
}
