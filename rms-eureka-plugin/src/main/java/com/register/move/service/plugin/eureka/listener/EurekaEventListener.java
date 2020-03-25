package com.register.move.service.plugin.eureka.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import com.register.move.service.listener.EventListener;
import com.register.move.service.plugin.eureka.event.EurekaEvent;
import lombok.extern.slf4j.Slf4j;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EurekaEventListener implements EventListener<EurekaEvent> {

    private  String tags;

    public EurekaEventListener(){
        tags = "tages";
    }

//    public static void main(String[] args) {
//        RestTemplateTransportClientFactory restTemplateTransportClientFactory =
//                new RestTemplateTransportClientFactory();
//        EurekaEndpoint eurekaEndpoint = new DefaultEndpoint("http://127.0.0.1:7003/eureka/");
//        EurekaHttpClient eurekaHttpClient = restTemplateTransportClientFactory.newClient(eurekaEndpoint);
//        EurekaHttpResponse<Application> eureka0 = eurekaHttpClient.getApplication("eureka0");
//        InstanceInfo instanceInfo = eureka0.getEntity().getInstances().get(0);
//        EurekaInstanceConverter forEureka = new EurekaInstanceConverter(instanceInfo);
//        StandardServiceMetadata converter = forEureka.converter();
//        converter.setId(UuidUtils.generateUuid());
//        SynchronizerHandler handler = new SynchronizerHandler();
//        handler.sync(forEureka,null);
//        InstanceUseForNacos forNacos = new InstanceUseForNacos();
//        InstanceUseForNacos forNacos1 = forNacos.convertT(converter);
//        System.out.println(forNacos1 instanceof Instance);
//        System.out.println(converter);
//    }

    @Deprecated
    public void sync(Converter converter, RegisterClient client) {
        StandardServiceMetadata metadata = converter.converter();
        try {
            ConfigService configService = NacosFactory.createConfigService("106.13.201.243:8848");
            if (configService.publishConfig(metadata.getId(),"default", JSON.toJSONString(metadata))) {
                System.out.println(" success");
            }
            String aDefault = configService.getConfig(metadata.getId(), "default", 5000);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        //TODO 1.read file
        //TODO 2.converter
        client.registerInstance(converter);//3.TODO
    }
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EurekaEvent event) {
        try {
            Class<?>  converterClassName = Class.forName(event.getDestConvertClassName());
            List<Object> instanceInfos = new ArrayList<>();
            event.getMetaData().forEach(metadata -> {
                instanceInfos.add(event.buildSyncInstance(metadata,metadata.getGroup()));
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

}
