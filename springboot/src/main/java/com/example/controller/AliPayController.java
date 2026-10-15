package com.example.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.example.common.config.AliPayConfig;
import com.example.entity.Film;
import com.example.entity.Orders;
import com.example.mapper.FilmMapper;
import com.example.mapper.OrdersMapper;
import com.example.service.OrdersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private OrdersService orderService;

    /**
     * 支付接口请求生成
     */
    @GetMapping("/pay")
    public void pay(@RequestParam String orderNo, HttpServletResponse httpResponse) throws Exception {
        Orders orders = orderService.selectByOrderNo(orderNo);
        if (orders == null) {
            httpResponse.setStatus(404);
            return;
        }

        Film film = filmMapper.selectById(orders.getFilmId());
        orders.setFilmName(film != null ? film.getTitle() : "未知电影");

        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());
        bizContent.set("total_amount", orders.getPrice());
        bizContent.set("subject", orders.getFilmName());
        bizContent.set("body", "购买电影票：" + orders.getFilmName());
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            log.error("支付宝请求生成失败，订单号：{}", orderNo, e);
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 支付异步回调接口
     */
    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws Exception {
        log.info("收到支付宝异步回调");

        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }

        String tradeStatus = params.get("trade_status");
        log.info("交易状态：{}", tradeStatus);

        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8");

            if (checkSignature) {
                log.info("签名验证通过，处理订单：{}", params.get("out_trade_no"));

                String tradeNo = params.get("out_trade_no");
                Orders orders = orderService.selectByOrderNo(tradeNo);
                if (orders != null) {
                    orders.setStatus("待取票");
                    orderService.updateById(orders);
                    log.info("订单 {} 状态更新为待取票", tradeNo);

                    Film film = filmMapper.selectById(orders.getFilmId());
                    if (film != null) {
                        film.setTotal(film.getTotal() + orders.getPrice());
                        filmMapper.updateById(film);
                        log.info("电影 {} 票房更新成功", film.getTitle());
                    }
                } else {
                    log.warn("回调订单不存在：{}", tradeNo);
                }
            } else {
                log.warn("支付宝签名验证失败");
            }
        }
    }
}
