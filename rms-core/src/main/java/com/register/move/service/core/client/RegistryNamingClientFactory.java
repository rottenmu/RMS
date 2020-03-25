package com.register.move.service.core.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.register.move.service.common.constants.RegisterClassConstant;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.config.RegisterClientConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

@Slf4j
public class RegistryNamingClientFactory {

    public static RegisterClient createServer(RegisterClientConfig config) throws Exception {
        Class<?> driverClass = null;
        try {
            driverClass = Class.forName(config.getClassName());
            Constructor<?> constructor = driverClass.getConstructor(RegisterClientConfig.class);
            RegisterClient registerClient = (RegisterClient) constructor.newInstance(config);
            return  registerClient;
        } catch (ClassNotFoundException e) {
            throw new NacosException(NacosException.CLIENT_INVALID_PARAM, e);
        }
    }


    public static void main(String[] args) {
        RegisterClientConfig registerConfig = new RegisterClientConfig();
        registerConfig.setServerAddr("http://127.0.0.1:7003/eureka/");
        registerConfig.setClassName(RegisterClassConstant.EUREKACLASSCONSTANT);
        RegistryNamingClientFactory factory = new RegistryNamingClientFactory();
        try {
            RegisterClient<Object, Object> server = factory.createServer(registerConfig);

            System.out.println(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
