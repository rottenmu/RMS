package com.register.move.service.mp.main;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.ConverterType;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.common.constants.TaskType;
import com.register.move.service.common.exception.RegistryMoveException;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.components.RegisterEventBus;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import com.register.move.service.event.BaseEvent;
import com.register.move.service.event.DaoEvent;
import com.register.move.service.listener.GenericEventListener;
import com.register.move.service.mp.config.RegisterMoveConfig;
import com.register.move.service.mp.utils.MPUtils;
import com.register.move.service.processor.ServiceMetaDataOperationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author  shengjie zhaao
 * @description when app starting get registry client,
 * and get service  instance from registry ,finally publish a BaseEvent{@link BaseEvent}
 */
@Slf4j
@Component
@Order(6)
public class ApplicationStartup implements CommandLineRunner {


    @Autowired
    private RegisterMoveConfig moveConfig;

    @Autowired
    private ServiceMetaDataOperationProcessor processor;

    @Override
    public void run(String... args) throws Exception {
        List<String> serviceNames = moveConfig.getServiceNames();
        if (CollectionUtils.isEmpty(serviceNames)) {
            throw  new RegistryMoveException(RegistryMoveException.NOT_EXIST,"serviceNames is null");
        }
        RegisterClient client = (RegisterClient) RegisterCache.getCache()
                .get(RegisterType.typeMap.get(moveConfig.getSourceRegisterType().toLowerCase()));
        String region = moveConfig.getRegion();
        String registerType = moveConfig.getSourceRegisterType().toLowerCase();
        ConverterType converterType = ConverterType.converterMap.get(registerType);
        String name = converterType.name();
        RegisterEventBus eventBus = RegisterEventBus.getEventBus(processor);
        String typeDesc = moveConfig.getDestinationRegisterType().toLowerCase();
        serviceNames.forEach(s -> {
            List instanceInfo = null;
            try {
                instanceInfo = client.serviceInstanceInfo(s, StringUtils.isNoneEmpty(region) ? region : null);
                if (CollectionUtils.isEmpty(instanceInfo)) {
                    log.error("{} Not registered on the server ",serviceNames);
                    Runtime.getRuntime().halt(-1);
                    throw  new RegistryMoveException(RegistryMoveException.NOT_EXIST,"There is no registered instance in the original registry");
                }
                   instanceInfo.stream().filter(Objects::nonNull).forEach(info ->{
                       try {
                           Constructor<?> constructor = Class.forName(converterType.getClassName()).getConstructor(info.getClass());
                           Converter converter = ((Converter) constructor.newInstance(info));
                           StandardServiceMetadata metadata = converter.converter();
                           DaoEvent daoEvent = new DaoEvent();
                           String dataId = MPUtils.dataId(metadata.getIp(), metadata.getServiceName(), metadata.getPort());
                           daoEvent.setDataId(dataId);
                           daoEvent.setGroup(moveConfig.getGroup());
                           metadata.setHostName(metadata.getIp());
                           daoEvent.setMetaData(Arrays.asList(metadata));
                           ConverterType destConverterType = ConverterType.converterMap.get(moveConfig.getDestinationRegisterType().toLowerCase());
                           daoEvent.setDestConvertClassName(destConverterType.getClassName());
                           daoEvent.setDestRegisterType(RegisterType.typeMap.get(typeDesc));
                           processor.setEventListener(new GenericEventListener());
                           BaseEvent event = new BaseEvent();
                           String className = TaskType.taskTypeMap.get(typeDesc).getClassName();
                           Class<?> destListenerClass = Class.forName(className);
                           event.setRunnable((Runnable) destListenerClass.newInstance());
                           processor.setBaseEvent(event);
                           eventBus.post(daoEvent);
                       } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                           e.printStackTrace();
                       } catch (ClassNotFoundException e) {
                           e.printStackTrace();
                       }
                   });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
