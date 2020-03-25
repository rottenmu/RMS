package com.register.move.service.plugin.zookeeper.domain;

import com.alibaba.fastjson.JSON;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ZookeeperInstanceConverter extends ZookeeperInstance implements Converter<ServiceInstance<ZookeeperInstance>> {


    private ServiceInstance<ZookeeperInstance> serviceInstance;

    public ZookeeperInstanceConverter(ServiceInstance<ZookeeperInstance> serviceInstance){
        this.serviceInstance = serviceInstance;
    }

    public ZookeeperInstanceConverter(StandardServiceMetadata metadata){
        convertT(metadata);
    }
    public ZookeeperInstanceConverter(){

    }
    public ZookeeperInstanceConverter(String id, String name, Map<String, String> metadata) {
        super(id, name, metadata);
    }

    @Override
    public StandardServiceMetadata converter() {
        StandardServiceMetadata metadata = new StandardServiceMetadata();
        metadata.setId(serviceInstance.getId());
        metadata.setServiceName(serviceInstance.getName());
        String json = JSON.toJSONString(this.serviceInstance.getPayload());
        LinkedHashMap data = JSON.parseObject(json, LinkedHashMap.class);
        Map metadata0 = (Map) data.get("metadata");
        metadata.setIp(String.valueOf(metadata0.get("ip")));
        metadata.setHostName(serviceInstance.getAddress());
        metadata.setPort(serviceInstance.getPort());
        metadata.setExtendedAttributes(this.getMetadata());
        metadata.setExtendedAttributes(data);
        return metadata;
    }

    @Override
    public ServiceInstance<ZookeeperInstance> convertT(StandardServiceMetadata metadata) {

        ZookeeperInstanceConverter forZookeeper = new ZookeeperInstanceConverter();
        forZookeeper.setId(metadata.getId());
        forZookeeper.setMetadata(metadata.getExtendedAttributes());
        forZookeeper.setName(metadata.getServiceName());
        ServiceInstance<ZookeeperInstance> serviceInstance = null;
        try {
            serviceInstance = ServiceInstance.<ZookeeperInstance>builder()
                    .name(metadata.getServiceName()).address(metadata.getIp())
                    .port(metadata.getPort()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.serviceInstance = serviceInstance;
        return serviceInstance;
    }

    public ServiceInstance<ZookeeperInstance> getServiceInstance() {
        return serviceInstance;
    }
}
