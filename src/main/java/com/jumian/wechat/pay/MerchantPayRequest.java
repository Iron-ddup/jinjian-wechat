package com.jumian.wechat.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MerchantPayRequest {

    @JsonProperty("partner_trade_no")
    private String partnerTradeNo;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("spbill_create_ip")
    private String ip;

    @JsonProperty("mch_appid")
    private String subAppId;

    @JsonProperty("mchid")
    private String subMchId;


}
