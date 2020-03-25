package com.register.move.service.plugin.nacos.listener;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.domain.Converter;
import com.register.move.service.listener.EventListener;
import com.register.move.service.plugin.nacos.event.NacosEvent;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class NacosEventListener implements EventListener<NacosEvent> {

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NacosEvent event) {

        try {
            Class<?> converterClassName = Class.forName(event.getDestConvertClassName());
            event.getMetaData().forEach(metadata -> {
                try {
                    Constructor<?> constructor = converterClassName.getConstructor(metadata.getClass());
                    Converter converter = (Converter) constructor.newInstance(metadata);
                    RegisterClient registerClient = (RegisterClient) RegisterCache.getCache().get(event.getDestRegisterType());
                    registerClient.registerInstance(converter);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
