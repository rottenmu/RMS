package com.register.move.service.plugin.zookeeper.listener;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.domain.Converter;
import com.register.move.service.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class ZookeeperTask implements Runnable {

    protected BaseEvent event;

    public void invoke(BaseEvent event) {
        try {
            Class<?> converterClassName = Class.forName(event.getDestConvertClassName());
            event.getMetaData().forEach(metadata -> {
                try {
                    Constructor<?> constructor = converterClassName.getConstructor(metadata.getClass());
                    Converter converter = (Converter) constructor.newInstance(metadata);
                    RegisterClient registerClient = (RegisterClient) RegisterCache.getCache().get(event.getDestRegisterType());
                   // registerClient.registerInstance(converter.convertT(metadata));
                    registerClient.registerInstance(converter.convertT(metadata));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.invoke(this.event);
    }
}
