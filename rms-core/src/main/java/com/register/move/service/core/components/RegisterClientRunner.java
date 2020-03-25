//package com.register.move.service.core.components;
//
//import com.register.move.service.cache.RegisterCache;
//import com.register.move.service.config.RegisterMoveConfig;
//import com.register.move.service.constants.RegisterType;
//import com.register.move.service.core.RegisterClient;
//import com.register.move.service.core.config.RegisterClientConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Constructor;
//
//@Component
//@Order(5)
//@Slf4j
//public class RegisterClientRunner implements ApplicationRunner {
//
//
//
//    @Autowired
//    private RegisterMoveConfig moveConfig;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        Class<?> destDriverClass = Class.forName(moveConfig.getDestDriverClass());
//        RegisterClientConfig clientConfig = new RegisterClientConfig();
//        clientConfig.setServerAddr(moveConfig.getDestinationServerAddr());
//        Constructor<?> constructor = destDriverClass.getConstructor(RegisterClientConfig.class);
//        RegisterClient destClient = (RegisterClient)constructor.newInstance(clientConfig);
//
//        Class<?> sourceDriverClass = Class.forName(moveConfig.getSourceDriverClass());
//        Constructor<?> sourcConstructor = sourceDriverClass.getConstructor(RegisterClientConfig.class);
//        clientConfig.setServerAddr(moveConfig.getSourceServerAddr());
//        RegisterClient sourceClient = (RegisterClient) sourcConstructor.newInstance(clientConfig);
//        RegisterType destType = RegisterType.typeMap.get(moveConfig.getDestinationRegisterType().toLowerCase());
////        Instance instance = new Instance();
////        instance.setPort(3004);
////        instance.setServiceName("eureka0");
////        instance.setIp("192.168.43.42");
////        destClient.registerInstance(instance);
//
//        RegisterType sourceType = RegisterType.typeMap.get(moveConfig.getSourceRegisterType().toLowerCase());
//        RegisterCache.getCache().put(destType,destClient);
//        RegisterCache.getCache().put(sourceType,sourceClient);
//    }
//}
