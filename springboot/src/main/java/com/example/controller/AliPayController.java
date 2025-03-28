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
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private OrdersService orderService;

    // 支付接口请求生成
    @GetMapping("/pay")
    public void pay(@RequestParam String orderNo, HttpServletResponse httpResponse) throws Exception {
        // 获取订单信息
        Orders orders = orderService.selectByOrderNo(orderNo);


        // 通过电影ID查询电影信息
        int filmId = orders.getFilmId();
        Film film = filmMapper.selectById(filmId);  // 假设你有一个 filmService 来查询电影信息

        // 如果电影信息不存在，设置默认名称
        if (film != null) {
            orders.setFilmName(film.getTitle());  // 设置电影名称
        } else {
            orders.setFilmName("未知电影");  // 设置默认电影名称
        }

        System.out.println("订单信息：" + orders.toString());
        // 创建client对象,通过sdk初始化client对象
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request 并设置 Request 参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest(); // 发送请求的 Request 类
        request.setNotifyUrl(aliPayConfig.getNotifyUrl()); // 设置异步通知地址
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());  // 设置唯一订单号
        bizContent.set("total_amount", orders.getPrice());  // 设置订单金额（确保格式正确）
        bizContent.set("subject", orders.getFilmName());  // 设置商品名称（简短描述）
        bizContent.set("body", "购买电影票：" + orders.getFilmName());  // 设置订单描述
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 设置支付产品代码
        request.setBizContent(bizContent.toString());  // 设置业务内容
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用 SDK 生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form); // 直接将完整的表单 HTML 输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
        // 打印出生成的支付请求参数，确保它们是完整且正确的
        System.out.println("Request Params: " + bizContent.toString());

    }

    // 支付异步回调接口
    @PostMapping("/notify") // 注意这里必须是POST接口
    public void payNotify(HttpServletRequest request) throws Exception {
        // 确认请求是否到达
        System.out.println("==========收到支付宝异步回调==========");

        // 获取请求参数
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }

        // 打印请求的交易状态
        String tradeStatus = params.get("trade_status");
        System.out.println("Received trade_status: " + tradeStatus);

        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            System.out.println("==========支付宝交易成功回调==========");

            // 获取签名和内容
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8");

            if (checkSignature) {
                // 验签通过，处理业务逻辑
                System.out.println("签名验证通过，处理订单逻辑");

                System.out.println("交易名称：" + params.get("subject"));
                System.out.println("交易状态：" + params.get("trade_status"));
                System.out.println("支付宝交易凭证号：" + params.get("trade_no"));
                System.out.println("商户订单号：" + params.get("out_trade_no"));
                System.out.println("交易金额：" + params.get("total_amount"));
                System.out.println("买家支付宝唯一id：" + params.get("buyer_id"));
                System.out.println("买家付款时间：" + params.get("gmt_payment"));
                System.out.println("买家付款金额：" + params.get("buyer_pay_amount"));

                String tradeNo = params.get("out_trade_no");
                Orders orders = orderService.selectByOrderNo(tradeNo);
                if (orders != null) {
                    orders.setStatus("待取票");
                    orderService.updateById(orders); // 更新订单状态
                    System.out.println("订单状态更新为已支付");

                    // 更新票房
                    Film film = filmMapper.selectById(orders.getFilmId());
                    if (film != null) {
                        film.setTotal(film.getTotal() + orders.getPrice());
                        filmMapper.updateById(film); // 更新电影票房
                        System.out.println("电影票房更新成功");
                    }
                } else {
                    System.out.println("订单不存在");
                }
            } else {
                System.out.println("签名验证失败");
            }
        } else {
            System.out.println("交易状态非成功，不处理");
        }
    }


}