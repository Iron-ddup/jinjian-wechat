package com.jumian.wechat.pay.config;

import com.jumian.wechat.pay.api.WechatPayClient;
import com.jumian.wechat.pay.properties.PayProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PayProperties.class)
public class WechatPayAutoConfig {
    @Autowired
    private PayProperties payProperties;

    @Bean
    public WechatPayClient buildPayClient(PayProperties payProperties) {
        WechatPayClient wechatPayClient = new WechatPayClient();
        wechatPayClient.setPayProperties(payProperties);
        return wechatPayClient;
    }
}
