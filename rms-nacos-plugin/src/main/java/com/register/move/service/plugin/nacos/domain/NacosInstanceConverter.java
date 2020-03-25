package com.register.move.service.plugin.nacos.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.util.UuidUtils;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class NacosInstanceConverter extends Instance implements Converter<NacosInstanceConverter> {


    protected Instance instance;

    public NacosInstanceConverter() {

        instance = new Instance();
    }

    public NacosInstanceConverter(Instance instance) {

        NacosInstanceConverter converter = this.createObject(NacosInstanceConverter.class, instance);
        try {
            BeanUtils.copyProperties(this,converter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        this.instance = instance;
    }

    // TODO converT方法有误
    public NacosInstanceConverter(StandardServiceMetadata metadata) {
        this.instance = new Instance();
        this.instance = convertT(metadata);
    }

    @Override
    public StandardServiceMetadata converter() {
        String json = JSON.toJSONString(this);
        StandardServiceMetadata metadata = JSON.parseObject(json, StandardServiceMetadata.class);
        metadata.setId(this.getInstanceId());
        metadata.setHealthy(this.isHealthy());
        return metadata;
    }

    @Override
    public NacosInstanceConverter convertT(StandardServiceMetadata metadata) {
        this.instance.setInstanceId(metadata.getId());
        this.instance.setMetadata(metadata.getExtendedAttributes());
        String json = JSON.toJSONString(metadata);
        NacosInstanceConverter converter = JSON.parseObject(json, NacosInstanceConverter.class);
        this.instance = converter;
        return converter;
    }

    public Instance getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setInstanceId(UuidUtils.generateUuid());
        instance.setHealthy(false);
        instance.setWeight(2.5D);
        instance.setClusterName("eureka-Cluster");
        instance.setPort(3333);
        instance.setServiceName("name");
        //     NacosInstanceConverter forNacos = new NacosInstanceConverter(instance);
//        StandardServiceMetadata converter = forNacos.converter();
//        System.out.println(converter);
    }

    @Override
    public Object get() {
        return this.instance;
    }

    protected <T> T createObject(Class<T> clazz,Object source){

        String json = JSON.toJSONString(source);
        T t = JSON.parseObject(json, clazz);
        return  t;
    }
}
