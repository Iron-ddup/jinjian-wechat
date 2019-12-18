package com.jumian.wechat.vo.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MicroMerchantVO {

    //业务申请编号
    @JsonProperty("business_code")
    private String businessCode;

    //身份证人像面照片
    @JsonProperty("id_card_copy")
    private String idCardCopy;

    //身份证国徽面照片
    @JsonProperty("id_card_national")
    private String idCardNational;

    //身份证姓名
    @JsonProperty("id_card_name")
    private String idCardName;

    //身份证号码
    @JsonProperty("id_card_number")
    private String idCardNumber;

    //身份证有效期限
    @JsonProperty("id_card_valid_time")
    private String idCardValidTime;

    //开户名称
    @JsonProperty("account_name")
    private String accountName;

    //开户银行
    @JsonProperty("account_bank")
    private String accountBank;

    //开户银行省市编码
    @JsonProperty("bank_address_code")
    private String bankAddressCode;

    //开户银行全称（含支行）
    @JsonProperty("bank_name")
    private String bankName;

    //银行账号
    @JsonProperty("account_number")
    private String accountNumber;

    //门店名称
    @JsonProperty("store_name")
    private String storeName;

    //门店省市编码
    @JsonProperty("store_address_code")
    private String storeAddressCode;

    //门店街道名称
    @JsonProperty("store_street")
    private String storeStreet;

    //门店经度
    @JsonProperty("store_longitude")
    private String storeLongitude;

    //门店纬度
    @JsonProperty("store_latitude")
    private String storeLatitude;

    //门店门口照片
    @JsonProperty("store_entrance_pic")
    private String storeEntrancePic;

    //店内环境照片
    @JsonProperty("indoor_pic")
    private String indoorPic;

    //经营场地证明
    @JsonProperty("address_certification")
    private String addressCertification;

    //商户简称
    @JsonProperty("merchant_shortname")
    private String merchantShortname;

    //客服电话
    @JsonProperty("service_phone")
    private String servicePhone;

    //售卖商品/提供服务描述
    @JsonProperty("product_desc")
    private String productDesc;

    //费率
    @JsonProperty("rate")
    private String rate;

    //补充说明
    @JsonProperty("business_addition_desc")
    private String businessAdditionDesc;

    //补充材料
    @JsonProperty("business_addition_pics")
    private String businessAdditionPics;

    //联系人姓名
    @JsonProperty("contact")
    private String contact;

    //手机号码
    @JsonProperty("contact_phone")
    private String contactPhone;

    //联系邮箱
    @JsonProperty("contact_email")
    private String contactEmail;

}
