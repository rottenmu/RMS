package com.register.move.service.domain;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Slf4j
public class InstanceUseForNacos extends Instance implements Converter<InstanceUseForNacos> {


    private Instance instance;

    public InstanceUseForNacos(){

    }

    public InstanceUseForNacos(Instance instance){
        try {
            BeanUtils.copyProperties(instance,this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        this.instance = instance;
    }
    public InstanceUseForNacos(StandardServiceMetadata metadata){
        convertT(metadata);
    }
    @Override
    public StandardServiceMetadata converter() {
        StandardServiceMetadata metadata = new StandardServiceMetadata();
        metadata.setHealthy(this.isHealthy());
        try {
            BeanUtils.copyProperties(this,metadata);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        metadata.setId(this.getInstanceId());
        return  metadata;
    }

    @Override
    public InstanceUseForNacos convertT(StandardServiceMetadata metadata) {
        this.setInstanceId(metadata.getId());
        this.setMetadata(metadata.getExtendedAttributes());
        try {
            BeanUtils.copyProperties(metadata,this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Instance getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setInstanceId(UUID.randomUUID().toString());
        instance.setHealthy(false);
        instance.setWeight(2.5D);
        instance.setClusterName("eureka-Cluster");
        instance.setPort(3333);
        instance.setServiceName("name");
        InstanceUseForNacos forNacos = new InstanceUseForNacos(instance);
        StandardServiceMetadata converter = forNacos.converter();
        System.out.println(converter);
    }
}
