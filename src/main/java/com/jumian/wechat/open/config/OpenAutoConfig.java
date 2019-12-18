package com.jumian.wechat.open.config;

import com.jumian.wechat.open.api.WechatOpenClient;
import com.jumian.wechat.open.properties.OpenProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenProperties.class)
public class OpenAutoConfig {

        @Autowired
        private OpenProperties openProperties;

    @Bean
    public WechatOpenClient buildOpenClient(OpenProperties openProperties) {
        WechatOpenClient wechatOpenClient = new WechatOpenClient();
        wechatOpenClient.setOpenProperties(openProperties);
        return wechatOpenClient;
    }
}
