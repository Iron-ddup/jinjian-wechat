package com.jumian.wechat.pay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HynOrderRequest {

    //回调地址
    private String notifyUrl;

    //订单类型
    private String orderType;

    //支付方式 0 微信公众号支付， 2 支付宝服务窗支付，11 微信 小程序支付
    private String payType;

    //订单编号
    private String outTradeNo;

    //订单信息
    private String subject;

    //总金额
    private BigDecimal amount;

    private BigDecimal commission;

    private String commissionState;

    //openId
    private String openId;

    //微信appId
    private String wxAppid;
}
