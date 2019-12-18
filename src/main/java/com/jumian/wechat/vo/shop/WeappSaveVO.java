package com.jumian.wechat.vo.shop;

import lombok.Data;

@Data
public class WeappSaveVO {

    private Long id;

    //企业名
    private String name;

    //企业代码
    private String code;

    //企业代码类型（1：统一社会信用代码， 2：组织机构代码，3：营业执照注册号）
    private String codeType;

    //法人微信
    private String legalPersonaWechat;

    //法人姓名
    private String legalPersonaName;

    //第三方联系电话
    private String componentPhone;
}
