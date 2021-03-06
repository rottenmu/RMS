//package com.register.move.service.core.components;
//
//import com.register.move.plugin.cache.RegisterCache;
//import com.register.move.plugin.config.RegisterMoveConfig;
//import com.register.move.plugin.constants.ConverterType;
//import com.register.move.plugin.constants.RegisterType;
//import com.register.move.plugin.core.RegisterClient;
//import com.register.move.plugin.domain.Converter;
//import com.register.move.plugin.domain.StandardServiceMetadata;
//import com.register.move.plugin.event.DaoEvent;
//import com.register.move.plugin.exception.RegistryMoveException;
//import com.register.move.plugin.handler.ServiceMetaDataOperationProcessor;
//import com.register.move.plugin.utils.NacosUtils;
//import com.register.move.service.config.RegisterMoveConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@Order(6)
//public class ApplicationStartup implements CommandLineRunner {
//
//
//    @Autowired
//    private RegisterMoveConfig moveConfig;
//
//    @Autowired
//    private ServiceMetaDataOperationProcessor processor;
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<String> serviceNames = moveConfig.getServiceNames();
//        if (CollectionUtils.isEmpty(serviceNames)) {
//            throw  new RegistryMoveException(RegistryMoveException.NOT_EXIST,"serviceNames is null");
//        }
//        RegisterClient client = (RegisterClient) RegisterCache.getCache()
//                .get(RegisterType.typeMap.get(moveConfig.getSourceRegisterType().toLowerCase()));
//        String region = moveConfig.getRegion();
//        String registerType = moveConfig.getSourceRegisterType().toLowerCase();
//        ConverterType converterType = ConverterType.converterMap.get(registerType);
//        String name = converterType.name();
//        RegisterEventBus eventBus = RegisterEventBus.getEventBus(processor);
//        String typeDesc = moveConfig.getDestinationRegisterType().toLowerCase();
//        serviceNames.forEach(s -> {
//            List instanceInfo = null;
//            try {
//                instanceInfo = client.serviceInstanceInfo(s, StringUtils.isNoneEmpty(region) ? region : null);
//                   instanceInfo.stream().filter(Objects::nonNull).forEach(info ->{
//                       try {
//                           Constructor<?> constructor = Class.forName(converterType.getClassName()).getConstructor(info.getClass());
//                           Converter converter = ((Converter) constructor.newInstance(info));
//                           StandardServiceMetadata metadata = converter.converter();
//                           DaoEvent daoEvent = new DaoEvent();
//                           String dataId = NacosUtils.dataId(metadata.getIp(), metadata.getServiceName(), metadata.getPort());
//                           daoEvent.setDataId(dataId);
//                           daoEvent.setGroup(moveConfig.getGroup());
//                           metadata.setHostName(metadata.getIp());
//                           daoEvent.setMetaData(Arrays.asList(metadata));
//                           ConverterType destConverterType = ConverterType.converterMap.get(moveConfig.getDestinationRegisterType().toLowerCase());
//                           daoEvent.setDestConvertClassName(destConverterType.getClassName());
//                           daoEvent.setDestRegisterType(RegisterType.typeMap.get(typeDesc));
//                           eventBus.post(daoEvent);
//                       } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                           e.printStackTrace();
//                       } catch (ClassNotFoundException e) {
//                           e.printStackTrace();
//                       }
//                   });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}
