package com.jumian.wechat.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UnifiedOrderRequest {

    //子商户appid
    @JsonProperty("sub_appid")
    private String subAppId;

    //子商户号
    @JsonProperty("sub_mch_id")
    private String subMchId;

    //商品描述
    private String body;

    //商户订单号
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    //总金额
    @JsonProperty("total_fee")
    private Integer totalFee;

    //终端IP
    @JsonProperty("spbill_create_ip")
    private String spbillCreateIp;

    //通知地址
    @JsonProperty("notify_url")
    private String notifyUrl;

    //交易类型
    @JsonProperty("trade_type")
    private String tradeType;

    //openid
    @JsonProperty("sub_openid")
    private String subOpenId;
}
