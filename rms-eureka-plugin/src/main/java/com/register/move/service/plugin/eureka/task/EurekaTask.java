package com.register.move.service.plugin.eureka.task;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import com.register.move.service.event.BaseEvent;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EurekaTask implements  Runnable {


    private BaseEvent event;

    @Override
    public void run() {
        try {
            Class<?>  converterClassName = Class.forName(event.getDestConvertClassName());
            List<Object> instanceInfos = new ArrayList<>();
            event.getMetaData().forEach(metadata -> {
                instanceInfos.add(this.buildSyncInstance(metadata,metadata.getGroup()));
            });
            instanceInfos.forEach(instanceInfo -> {
                try {
                    Constructor<?> constructor = converterClassName.getConstructor(instanceInfo.getClass());
                    Converter converter = (Converter) constructor.newInstance(instanceInfo);
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
    public InstanceInfo buildSyncInstance(StandardServiceMetadata instance, String group) {
        DataCenterInfo dataCenterInfo = new MyDataCenterInfo(DataCenterInfo.Name.MyOwn);
        HashMap<String, String> metadata = new HashMap<>(16);
        String homePageUrl = "http://" + instance.getIp() + ":" + instance.getPort()+"/";
        String serviceName = this.transformName(instance.getServiceName(),group);
        return new InstanceInfo(
                "LAPTOP-E9V666V4" + ":" + serviceName + ":" + instance.getPort(),
                serviceName,
                null,
                instance.getIp(),
                null,
                new InstanceInfo.PortWrapper(true, instance.getPort()),
                null,
                homePageUrl,
                homePageUrl + "actuator/info",
                homePageUrl + "actuator/health",
                null,
                serviceName,
                serviceName,
                1,
                dataCenterInfo,
                instance.getIp(),
                InstanceInfo.InstanceStatus.UP,
                InstanceInfo.InstanceStatus.UNKNOWN,
                null,
                new LeaseInfo(30, 90,
                        0L, 0L, 0L, 0L, 0L),
                false,
                metadata,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                InstanceInfo.ActionType.ADDED,
                null
        );
    }
    private    String transformName(String serviceName,String group){
        int index = serviceName.indexOf("@@")+"@@".length();
        String name = StringUtils.substring(serviceName, index, serviceName.length());
        return serviceName.contains("@@")?name:serviceName;
    }
}
