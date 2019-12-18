package com.jumian.wechat.open;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthorizerInfo {

    @JsonProperty("code")
    private String code;
}
