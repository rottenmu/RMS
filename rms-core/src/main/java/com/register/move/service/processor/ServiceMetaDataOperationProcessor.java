package com.register.move.service.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.eventbus.Subscribe;
import com.register.move.service.config.NacosConfig;
import com.register.move.service.dao.ServiceMetaDataOperation;
import com.register.move.service.event.BaseEvent;
import com.register.move.service.event.DaoEvent;

import com.register.move.service.listener.EventListener;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

public class ServiceMetaDataOperationProcessor implements ServiceMetaDataOperation {

    private NacosConfig config;
    private static ThreadLocal<ConfigService> threadLocal = new ThreadLocal<>();
    private volatile ConfigService configService;
    private final String DEF = "default";
    private final Long TIMEOUT = 5000L;
    private static volatile ThreadLocal<Object> LISTENER = new TransmittableThreadLocal<>();
    private static volatile ThreadLocal<BaseEvent> EVENT = new TransmittableThreadLocal<>();

    @Autowired
    public ServiceMetaDataOperationProcessor(NacosConfig config) {
        this.config = config;
        configService = init();
    }
    @Override
    @Subscribe
    public  boolean publish(DaoEvent event) {
        String group = event.getGroup();
        group = StringUtils.isNoneBlank() ? group : DEF;
        try {

            // configService.addListener(event.getDataId(),group,new SynchronizerHandler());
            boolean result = configService.publishConfig(event.getDataId(), group, JSON.toJSONString(event));
            if (result) {
                EventBus eventBus =  EventBus.getDefault();
                LISTENER.get().getClass().newInstance();
                eventBus.register( LISTENER.get().getClass().newInstance());
                BaseEvent baseEvent = EVENT.get();
                Class<? extends Runnable> runnableClass = baseEvent.getRunnable().getClass();
                Runnable runnable = runnableClass.newInstance();
                Field field = runnableClass.getDeclaredField("event");
                field.setAccessible(true);
                field.set(runnable,baseEvent);
                baseEvent.setRunnable(runnable);
                BeanUtils.copyProperties(event, baseEvent);
                eventBus.post(baseEvent);
            }
            return result;
        } catch (NacosException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(DaoEvent event) {
        try {
            return configService.removeConfig(event.getDataId(), transformGroup(event.getGroup()));
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String get(DaoEvent event) {

        long timeoutMs = config.getTimeoutMs();
        timeoutMs = timeoutMs > 1000 ? timeoutMs : TIMEOUT;
        try {
            return configService.getConfig(event.getDataId(), transformGroup(event.getGroup()), timeoutMs);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    protected String transformGroup(String group) {
        return StringUtils.isNoneBlank() ? group : DEF;
    }

    protected ConfigService init() {
        try {
            if (Objects.isNull(threadLocal.get())) {
                configService = NacosFactory.createConfigService(config.getServerAddr());
                return configService;
            } else {
                return threadLocal.get();
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return configService;
    }

    public void setBaseEvent(BaseEvent baseEvent) {

        EVENT.set(baseEvent);
    }

    public void setEventListener(EventListener eventListener) {
        LISTENER.set(eventListener);
    }

}
