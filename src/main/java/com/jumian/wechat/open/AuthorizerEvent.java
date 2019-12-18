package com.jumian.wechat.open;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 授权事件响应
 */

@Data
public class AuthorizerEvent {

    @JsonProperty("AppId")
    private String appId;

    @JsonProperty("InfoType")
    private String infoType;

    @JsonProperty("AuthorizerAppid")
    private String authorizerAppId;

    @JsonProperty("AuthorizationCode")
    private String authorizationCode;

    @JsonProperty("PreAuthCode")
    private String preAuthCode;

    @JsonProperty("ComponentVerifyTicket")
    private String componentVerifyTicket;

    @JsonProperty("status")
    private String status;

    @JsonProperty("appid")
    private String weappId;

    @JsonProperty("info")
    private AuthorizerInfo authorizerInfo;
}
