package com.jumian.wechat.pay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefundRequest {

    //商户退款单号
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    //商户订单号
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    //退款金额
    @JsonProperty("refund_fee")
    private Integer refundFee;

    //总金额
    @JsonProperty("total_fee")
    private Integer totalFee;

    //退款原因
    @JsonProperty("refund_desc")
    private String refundDesc;
}
