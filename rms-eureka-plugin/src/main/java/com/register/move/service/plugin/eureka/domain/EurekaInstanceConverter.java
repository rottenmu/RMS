package com.register.move.service.plugin.eureka.domain;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.register.move.service.core.components.ServiceMetadataConverter;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cloud.netflix.eureka.InstanceInfoFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Map;

public class EurekaInstanceConverter extends InstanceInfo implements Converter<EurekaInstanceConverter> {

    private InstanceInfo instanceInfo;

    public EurekaInstanceConverter(InstanceInfo instanceInfo) {
        super(instanceInfo);
        this.instanceInfo = instanceInfo;
    }

    @Override
    public StandardServiceMetadata converter() {
        Iterable<InstanceInfo> iterable = Arrays.asList(this);
        ListIterator<StandardServiceMetadata> standardSrviceMetadataListIterator = ServiceMetadataConverter.
                converterStandard(iterable);
        return standardSrviceMetadataListIterator.next();
    }

    @Override
    public EurekaInstanceConverter convertT(StandardServiceMetadata metadata) {
        InstanceInfoFactory instanceInfoFactory = new InstanceInfoFactory();
        MyDataCenterInstanceConfig config = new MyDataCenterInstanceConfig();
        InstanceInfo instanceInfo = instanceInfoFactory.create(config);
        EurekaInstanceConverter forEureka = new EurekaInstanceConverter(instanceInfo);
        Builder builder = Builder.newBuilder();
        builder.setInstanceId(metadata.getId());
        builder.setStatus(metadata.getHealthy() == true ? InstanceStatus.UP : InstanceStatus.DOWN);
        builder.setMetadata(metadata.getExtendedAttributes());
        builder.setAppName(metadata.getServiceName());
        // TODO
        try {
            BeanUtils.copyProperties(metadata, builder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return forEureka;
    }

    public static InstanceInfo createInstance(String appName, String hostName, int port, Map metadata) {
        InstanceInfo instanceInfo = Builder.newBuilder().setAppName(appName)
                .setHostName(hostName).setPort(port).build();
        try {
            Field field = instanceInfo.getClass().getDeclaredField("metadata");
            field.setAccessible(true);
            field.set(instanceInfo, metadata);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new InstanceInfo(instanceInfo);
    }

    public static void main(String[] args) {

//        RestTemplateTransportClientFactory restTemplateTransportClientFactory =
//                new RestTemplateTransportClientFactory();
//        EurekaEndpoint eurekaEndpoint = new DefaultEndpoint("http://127.0.0.1:7003/eureka/");
//        EurekaHttpClient eurekaHttpClient = restTemplateTransportClientFactory.newClient(eurekaEndpoint);
//        EurekaHttpResponse<Application> eureka0 = eurekaHttpClient.getApplication("eureka0");
//        InstanceInfo instanceInfo = eureka0.getEntity().getInstances().get(0);
//        EurekaInstanceConverter forEureka = new EurekaInstanceConverter(instanceInfo);
//        StandardServiceMetadata converter = forEureka.converter();
//        System.out.println(converter);
    }

    public InstanceInfo getInstanceInfo() {
        return instanceInfo;
    }
}
