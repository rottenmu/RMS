package com.register.move.service.core;

import com.register.move.service.core.config.RegisterClientConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final  String driver = "com.register.move.service.plugin.nacos.client.NacosNamingClient";
        final  String driver0 = "com.register.move.plugin.client.eureka.EurekaNamingService";
        RegisterClientConfig registerConfig = new RegisterClientConfig();
        registerConfig.setServerAddr("http://127.0.0.1:7003/eureka/");
        Class<?> aClass = Class.forName(driver0);
        Constructor<?> constructor = aClass.getConstructor(RegisterClientConfig.class);
        RegisterClient registerClient = (RegisterClient) constructor.newInstance(registerConfig);
        System.out.println(registerClient);

    }
}
