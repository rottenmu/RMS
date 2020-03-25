package com.register.move.service.plugin.nacos.task;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.domain.Converter;
import com.register.move.service.event.BaseEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NacosTask implements Runnable {

    private BaseEvent event;

    @Override
    public void run() {
        try {
            Class<?> converterClassName = Class.forName(event.getDestConvertClassName());
            event.getMetaData().forEach(metadata -> {
                try {
                    Constructor<?> constructor = converterClassName.getConstructor(metadata.getClass());
                    Converter converter = (Converter) constructor.newInstance(metadata);
              //      Object convertT = converter.convertT(metadata);
                    RegisterClient registerClient = (RegisterClient) RegisterCache.getCache().get(event.getDestRegisterType());
                    registerClient.registerInstance(converter.get());
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
