package com.jumian.wechat.pay.api;


import com.jumian.wechat.open.properties.OpenProperties;
import com.jumian.wechat.pay.MerchantPayRequest;
import com.jumian.wechat.pay.RefundRequest;
import com.jumian.wechat.pay.constants.PayUrlConstants;

import com.jumian.wechat.pay.properties.PayProperties;
import com.jumian.wechat.utils.JsonUtils;
import com.jumian.wechat.utils.RSAEncrypt;
import com.jumian.wechat.utils.WechatPayUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(OpenProperties.class)
public class WechatPayClient {
    Log log = LogFactory.getLog(WechatPayClient.class);

    @Resource
    private PayProperties payProperties;

    public void setPayProperties(PayProperties payProperties) {
        this.payProperties = payProperties;
    }

    /**
     * 获取证书
     */
    public Map<String, String> getCertficates() {
        try {
            Map<String, String> reqData = new HashMap<>();
          //  reqData.put("appid", payProperties.getAppId());
           // reqData.put("appid", "wx5376b43d263ad2c6");
           // reqData.put("mch_id", payProperties.getMchId());
            reqData.put("mch_id", "1568100021");
            reqData.put("nonce_str", WechatPayUtil.generateNonceStr());
            reqData.put("sign_type", "HMAC-SHA256");
          //  reqData.put("sign", WechatPayUtil.generateSignature(reqData, payProperties.getKey()));
            reqData.put("sign", WechatPayUtil.generateSignature(reqData, "666879a1f5d0e6735cf74c04214d92c3"));
            System.out.println(reqData.toString());
            String respXml = WechatPayUtil.requestWithoutCert(PayUrlConstants.GET_CERTFICATES_URL, this.fillRequestData(reqData));
            log.info("或者证书响应:" + respXml);
            return this.responseXml(respXml);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取官方证书 {}", e);
        }
    }



    /**
     * 媒体文件上传
     */
    public String uploadMedia(MultipartFile multipartFile) {
        try {
            Map<String, String> reqData = new HashMap<>();
            byte[] uploadBytes = multipartFile.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);
            reqData.put("media_hash", hashString);
            reqData.put("mch_id", payProperties.getMchId());
            reqData.put("sign_type", "HMAC-SHA256");
            reqData.put("sign", WechatPayUtil.generateSignature(reqData, payProperties.getKey()));
            String respXml = WechatPayUtil.uploadWithCert(PayUrlConstants.UPLOAD_MEDIA_URL, reqData, payProperties.getMchId(), payProperties.getCertPath(), multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            log.info("上传文件响应：" + respXml);
            return WechatPayUtil.xmlToMap(respXml).get("media_id");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败 {}", e);
        }
    }

    /**
     * 商户进件
     */
    public boolean microMerchantSubmit(String json) {
        Map<String, String> map = JsonUtils.jsonToMapString(json);
        map.put("version", "3.0");
        map.put("cert_sn", payProperties.getCertSn());
        map.put("id_card_name", RSAEncrypt.rsaEncrypt(map.get("id_card_name"), payProperties.getPemCertPath()));
        map.put("id_card_number", RSAEncrypt.rsaEncrypt(map.get("id_card_number"), payProperties.getPemCertPath()));
        map.put("account_name", RSAEncrypt.rsaEncrypt(map.get("account_name"), payProperties.getPemCertPath()));
        map.put("account_number", RSAEncrypt.rsaEncrypt(map.get("account_number"), payProperties.getPemCertPath()));
        map.put("contact", RSAEncrypt.rsaEncrypt(map.get("contact"), payProperties.getPemCertPath()));
        map.put("contact_phone", RSAEncrypt.rsaEncrypt(map.get("contact_phone"), payProperties.getPemCertPath()));
        map.put("contact_email", RSAEncrypt.rsaEncrypt(map.get("contact_email"), payProperties.getPemCertPath()));

        try {
            String respXml = WechatPayUtil.requestWithCert(PayUrlConstants.MICRO_MERCHANT_SUBMIT_URL, this.fillRequestData(map), payProperties.getMchId(), payProperties.getCertPath());
            log.info("小微商户进件响应：" + respXml);
            return WechatPayUtil.xmlToMap(respXml).get("return_code").equals("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("商户进件失败：{}", e);
        }
        return true;
    }

    /**
     * 小微商户进件查询
     */
    public boolean getMicroMerchantState(String applymentId) {
        Map<String, String> map = new HashMap<>();
        map.put("version", "1.0");
        map.put("applyment_id", applymentId);
        try {
            String respXml = WechatPayUtil.requestWithCert(PayUrlConstants.GET_MICRO_MERCHANT_STATES_URL, this.fillRequestData(map), payProperties.getMchId(), payProperties.getCertPath());
            log.info("小微商户进件查询响应：" + respXml);
            return WechatPayUtil.xmlToMap(respXml).get("return_code").equals("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("小微商户进件查询失败：{}", e);
        }
        return true;
    }


    /**
     * 微信退款
     */
    public Map<String, String> refund(RefundRequest request, String subMchId) {
        Map<String, String> map = JsonUtils.jsonToMapString(JsonUtils.objectToJson(request));
        map.put("sub_mch_id", subMchId);
        try {
            String respXml = WechatPayUtil.requestWithCert(PayUrlConstants.REFUND_URL, this.fillRequestDataWithAppId(map), payProperties.getMchId(), payProperties.getCertPath());
            log.info("微信退款：" + respXml);
            return WechatPayUtil.xmlToMap(respXml);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信退款失败：{}", e);
        }
        return null;
    }

    /**
     * 企业付款
     */
    public Map<String, String> merchantPay(MerchantPayRequest request, String key, String certPath) {
        Map<String, String> reqData = JsonUtils.jsonToMapString(JsonUtils.objectToJson(request));
        reqData.put("check_name", "NO_CHECK");
        reqData.put("nonce_str", WechatPayUtil.generateNonceStr());

        try {
            reqData.put("sign", WechatPayUtil.generateSignature(reqData, key));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String respXml = WechatPayUtil.requestWithCert(PayUrlConstants.MERCHANT_PAY_URL, reqData, request.getSubMchId(), certPath);
            log.info("企业支付到零钱：" + respXml);
            return WechatPayUtil.xmlToMap(respXml);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("企业支付到零钱失败：{}", e);
        }
        return null;
    }



    /**
     * 填充数据
     */
    private Map<String, String> fillRequestDataWithAppId(Map<String, String> reqData) {
        reqData.put("appid", payProperties.getAppId());
        reqData.put("mch_id", payProperties.getMchId());
        reqData.put("nonce_str", WechatPayUtil.generateNonceStr());
        reqData.put("sign_type", "HMAC-SHA256");
        try {
            reqData.put("sign", WechatPayUtil.generateSignature(reqData, payProperties.getKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reqData;
    }

    /**
     * 填充数据
     */
    private Map<String, String> fillRequestData(Map<String, String> reqData) {
        reqData.put("mch_id", payProperties.getMchId());
        reqData.put("nonce_str", WechatPayUtil.generateNonceStr());
        reqData.put("sign_type", "HMAC-SHA256");
        try {
            reqData.put("sign", WechatPayUtil.generateSignature(reqData, payProperties.getKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reqData;
    }

    /**
     * 响应的xml转map
     */
    private Map<String, String> responseXml(String respXml) {
        Map<String, String> map = new HashMap<>();
        try {
            map = WechatPayUtil.processResponseXml(respXml, payProperties.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }


}
