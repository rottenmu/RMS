package com.register.move.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rms.config" )
public class NacosConfig {

    private  String serverAddr;
    private  Long timeoutMs;
}
