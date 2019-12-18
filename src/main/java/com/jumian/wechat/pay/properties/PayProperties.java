package com.jumian.wechat.pay.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wechat.pay", ignoreInvalidFields = true)
public class PayProperties {

    /**
     * 微信支付绑定的服务号appID
     */
    private String appId;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 微信支付密钥
     */
    private String key;

    /**
     * 微信证书地址
     */
    private String certPath;

    /**
     * 平台证书序列号
     */
    private String certSn;

    /**
     * 平台证书路径
     */
    private String pemCertPath;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public String getPemCertPath() {
        return pemCertPath;
    }

    public void setPemCertPath(String pemCertPath) {
        this.pemCertPath = pemCertPath;
    }
}