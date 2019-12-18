package com.jumian.wechat.open;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 客服消息响应
 */

@Data
public class CustomMsg {

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("MsgType")
    private String msgType;

    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private String msgId;

    @JsonProperty("Event")
    private String event;

    @JsonProperty("Reason")
    private String reason;
}
