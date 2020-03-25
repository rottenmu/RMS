package com.register.move.service.mp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rms.config" )
public class NacosConfig  extends com.register.move.service.config.NacosConfig {

    private  String serverAddr;
    private  Long timeoutMs;
}
