package com.jumian.wechat.open.api;

import com.jumian.wechat.open.AuthorizerEvent;
import com.jumian.wechat.open.CustomMsg;
import com.jumian.wechat.open.constants.OpenUrlConstants;
import com.jumian.wechat.open.properties.OpenProperties;
import com.jumian.wechat.utils.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WechatOpenClient {
    Log log = LogFactory.getLog(WechatOpenClient.class);

    private OpenProperties openProperties;

    public void setOpenProperties(OpenProperties openProperties) {
        this.openProperties = openProperties;
    }

    public OpenProperties getOpenProperties() {
        return openProperties;
    }

    /**
     * 微信消息解密出票据
     */
    public AuthorizerEvent decryptEvent(String xmlStr, String timestamp, String nonce, String msgSignature) {
        WechatMsgCryptUtil wxBizMsgCryptUtil;
        Map<String, Object> decryptMsgMap = new HashMap<>();

        try {
            wxBizMsgCryptUtil = new WechatMsgCryptUtil(openProperties.getToken(), openProperties.getEncodingAesKey(), openProperties.getAppId());

            String decryptMsgXml = wxBizMsgCryptUtil.decryptMsg(msgSignature, timestamp, nonce, xmlStr);
            log.info("解密消息:" + decryptMsgXml);

            decryptMsgMap = XmlUtil.xml2map(decryptMsgXml, false);
        } catch (AesException e) {
            e.printStackTrace();
            log.error("解密失败", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.jsonToBean(JsonUtils.objectToJson(decryptMsgMap), AuthorizerEvent.class);
    }

    /**
     * 微信客户消息解密
     */
    public CustomMsg decryptMsg(String xmlStr, String timestamp, String nonce, String msgSignature) {
        WechatMsgCryptUtil wxBizMsgCryptUtil;
        Map<String, String> decryptMsgMap = new HashMap<>();

        try {
            wxBizMsgCryptUtil = new WechatMsgCryptUtil(openProperties.getToken(), openProperties.getEncodingAesKey(), openProperties.getAppId());

            String decryptMsgXml = wxBizMsgCryptUtil.decryptMsg(msgSignature, timestamp, nonce, xmlStr);
            log.info("解密消息:" + decryptMsgXml);

            decryptMsgMap = WechatPayUtil.xmlToMap(decryptMsgXml);
        } catch (AesException e) {
            e.printStackTrace();
            log.error("解密失败", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.jsonToBean(JsonUtils.objectToJson(decryptMsgMap), CustomMsg.class);
    }

    /**
     * 票据换取accessToken
     */
    public String getComponentAccessToken(String ticket) {

        Map<String, String> postMap = new HashMap<>();
        postMap.put("component_appid", openProperties.getAppId());
        postMap.put("component_appsecret", openProperties.getSecret());
        postMap.put("component_verify_ticket", ticket);

        String reStr = "";
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.ACCESS_TOKEN_URL, JsonUtils.objectToJson(postMap));
        } catch (Exception e) {
            log.error("微信第三方服务平台获取令牌网关错误！");
            e.printStackTrace();
        }

        Map<String, Object> reMap = JsonUtils.jsonToMap(reStr);

        String componentAccessToken = (String) reMap.get("component_access_token");
        return componentAccessToken;
    }

    /**
     * 换取预授权码
     */
    public String getPreAuthCode(String componentAccessToken) {
        Map<String, String> postMap = new HashMap<>();
        postMap.put("component_appid", openProperties.getAppId());

        String reStr;

        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.PRE_AUTH_CODE_URL + "?component_access_token=" + componentAccessToken, JsonUtils.objectToJson(postMap));
            Map<String, Object> reMap = JsonUtils.jsonToMap(reStr);
            String preAuthCode = (String) reMap.get("pre_auth_code");
            log.info("预授权码：" + preAuthCode);
            return preAuthCode;
        } catch (Exception e) {
            throw new RuntimeException("获取预授权码网关错误！{}", e);
        }
    }

    /**
     * 使用授权码换取公众号或小程序的接口调用凭据和授权信息
     */
    public Map<String, Object> getAuthorizerAccessToken(String authorizationCode, String componentAccessToken) {
        HashMap<String, String> postMap = new HashMap<>();
        postMap.put("component_appid", openProperties.getAppId());
        postMap.put("authorization_code", authorizationCode);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.QUERY_AUTH_URL + "?component_access_token=" + componentAccessToken, JsonUtils.objectToJson(postMap));
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            Map<String, Object> authorizationInfoMap = JsonUtils.jsonToMap(JsonUtils.objectToJson(map.get("authorization_info")));
            return authorizationInfoMap;
        } catch (Exception e) {
            throw new RuntimeException("获取用户授权令牌网关错误！{}", e);
        }
    }

    /**
     * 获取授权公众号或小程序的接口调用凭据（令牌）
     */
    public Map<String, Object> refreshAuthorizerAccessToken(String appId, String authorizerRefreshToken, String componentAccessToken) {
        HashMap<String, String> postMap = new HashMap<>();
        postMap.put("component_appid", openProperties.getAppId());
        postMap.put("authorizer_appid", appId);
        postMap.put("authorizer_refresh_token", authorizerRefreshToken);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.AUTHORIZER_TOKEN_URL + "?component_access_token=" + componentAccessToken, JsonUtils.objectToJson(postMap));
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map;
        } catch (Exception e) {
            throw new RuntimeException("刷新授权令牌网关错误！{}", e);
        }
    }

    public boolean pushCode(String authorizerAccessToken, String extJson, Integer templateId, String userVersion, String userDesc) {

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("template_id", templateId);
        postMap.put("ext_json", extJson);
        postMap.put("user_version", userVersion);
        postMap.put("user_desc", userDesc);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.COMMIT_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("刷新授权令牌网关错误！{}", e);
        }
    }

    public Integer submitAudit(String authorizerAccessToken, List list) {

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("item_list", list);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.SUBMIT_AUDIT_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return (Integer) map.get("auditid");
        } catch (Exception e) {
            throw new RuntimeException("小程序提交审核错误！{}", e);
        }
    }

    public String getAuditStatus(String authorizerAccessToken, Integer auditId) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("auditid", auditId);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.GET_AUDITSTATUS_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("status").toString();
        } catch (Exception e) {
            throw new RuntimeException("获取小程序审核状态！{}", e);
        }
    }

    public InputStream getWeappQrCode(String authorizerAccessToken, String path) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("path", path);
        try {
            byte[] data = WechatHttpUtil.download(OpenUrlConstants.GET_WXACODE_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            InputStream inputStream = new ByteArrayInputStream(data);
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException("获取小程序二维码！{}", e);
        }
    }

    public InputStream getWeappQrCode(String authorizerAccessToken, String page, String scene) {
        if (page.startsWith("/")) {
            page = page.substring(1);
        }

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("page", page);
        postMap.put("scene", scene);
        try {
            byte[] data = WechatHttpUtil.download(OpenUrlConstants.GET_WXACODE_UNLIMIT_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            InputStream inputStream = new ByteArrayInputStream(data);
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException("获取小程序二维码！{}", e);
        }
    }


    public boolean setNickName(String authorizerAccessToken, String nickName) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("nick_name", nickName);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.SET_NICKNAME_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("设置小程序名称！{}", e);
        }
    }

    public boolean modifyHeadImg(String authorizerAccessToken, String headImgMediaId) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("head_img_media_id", headImgMediaId);
        postMap.put("x1", 0);
        postMap.put("y1", 0);
        postMap.put("x2", 1);
        postMap.put("y2", 1);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.MODIFY_HEAD_IMAGE_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("设置小程序头像！{}", e);
        }
    }

    public boolean release(String authorizerAccessToken) {
        HashMap<String, Object> postMap = new HashMap<>();

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.RELEASE_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("发布小程序！{}", e);
        }
    }


    /**
     * 添加微信服务通知模板
     */
    public String addTemp(String componentAccessToken, String tempLibId, List<Long> keywordIdList) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("id", tempLibId);
        postMap.put("keyword_id_list", keywordIdList);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.ADD_TEMP_URL + "?access_token=" + componentAccessToken, JsonUtils.objectToJson(postMap));
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return (String) map.get("template_id");
        } catch (Exception e) {
            throw new RuntimeException("添加服务通知模板错误！{}", e);
        }
    }

    public boolean tempSend(String authorizerAccessToken, String postJson) {
        String reStr;
        try {
            log.info("接口请求数据:" + postJson);
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.TEMP_SEND_URL + "?access_token=" + authorizerAccessToken, postJson);
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("发送模板！{}", e);
        }
    }

    public boolean modifySignature(String authorizerAccessToken, String signature) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("signature", signature);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.MODIFY_SIGNATURE_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("修改小程序功能介绍！{}", e);
        }
    }

    public boolean addCategory(String authorizerAccessToken, List list) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("categories", list);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.ADD_CATEGORY_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("小程序添加类目！{}", e);
        }
    }

    public boolean modifyDomain(String authorizerAccessToken, List<String> domainList) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("action", "add");
        postMap.put("requestdomain", domainList);
        postMap.put("downloaddomain", domainList);
        postMap.put("uploaddomain", domainList);

        String reStr;
        try {
            reStr = WechatHttpUtil.httpPost(OpenUrlConstants.MODIFY_DOMAIN_URL + "?access_token=" + authorizerAccessToken, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("设置小程序服务器域名！{}", e);
        }
    }

    /**
     * 媒体文件上传
     */
    public String mediaUpload(String authorizerAccessToken, MultipartFile multipartFile) {
        try {
            String s = WechatHttpUtil.upload(OpenUrlConstants.MEDIA_UPLOAD_URL + "?access_token=" + authorizerAccessToken + "&type=image", multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            log.info("上传文件响应：" + s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败 {}", e);
        }
    }

    /**
     * code解析出session
     */
    public Map<String, Object> code2Session(String appId, String jsCode, String componentAccessToken) {
        String reStr;
        try {
            String url = String.format(OpenUrlConstants.CODE2SESSION_URL, appId, jsCode, openProperties.getAppId(), componentAccessToken);
            reStr = WechatHttpUtil.httpPost(url, null);
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map;
        } catch (Exception e) {
            throw new RuntimeException("获取openId失败！{}", e);
        }
    }

    /**
     * 快速创建小程序
     */
    public boolean fastRegisterWeapp(String name, String code, String codeType, String legalPersonaWechat, String legalPersonaName, String componentPhone, String componentAccessToken) {
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("name", name);
        postMap.put("code", code);
        postMap.put("code_type", codeType);
        postMap.put("legal_persona_wechat", legalPersonaWechat);
        postMap.put("legal_persona_name", legalPersonaName);
        postMap.put("component_phone", componentPhone);

        String reStr;
        try {
            String url = String.format(OpenUrlConstants.FAST_REGISTER_WEAPP_URL, componentAccessToken);
            reStr = WechatHttpUtil.httpPost(url, JsonUtils.objectToJson(postMap));
            log.info("微信接口返回" + reStr);
            Map<String, Object> map = JsonUtils.jsonToMap(reStr);
            return map.get("errcode").equals(0);
        } catch (Exception e) {
            throw new RuntimeException("快速创建小程序失败 {}", e);
        }
    }
}
