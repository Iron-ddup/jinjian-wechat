package com.jumian.wechat.pay.constants;

public class PayUrlConstants {

    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";

    //统一下单
    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    public static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

    //退款
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    public static final String DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
    public static final String SHORTURL_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";

    //获取平台证书
    public static final String GET_CERTFICATES_URL = "https://api.mch.weixin.qq.com/risk/getcertficates";

    //上传媒体文件
    public static final String UPLOAD_MEDIA_URL = "https://api.mch.weixin.qq.com/secapi/mch/uploadmedia";

    //上传媒体文件
    public static final String MICRO_MERCHANT_SUBMIT_URL = "https://api.mch.weixin.qq.com/applyment/micro/submit";

    //上传媒体文件
    public static final String GET_MICRO_MERCHANT_STATES_URL = "https://api.mch.weixin.qq.com/applyment/micro/getstate";

    //APPID关联
    public static final String ADD_SUB_DEV_CONFIG_URL = "https://api.mch.weixin.qq.com/secapi/mch/addsubdevconfig";

    //企业付款
    public static final String MERCHANT_PAY_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
}
