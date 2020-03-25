package com.register.move.service.core.components;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

public class ConfigClient {

    public static void main(String[] args) {
        try {
            ConfigService configService = NacosFactory.createConfigService("106.13.201.243:8848");
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
