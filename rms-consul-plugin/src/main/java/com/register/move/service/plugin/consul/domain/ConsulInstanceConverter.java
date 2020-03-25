package com.register.move.service.plugin.consul.domain;

import com.alibaba.fastjson.JSON;
import com.ecwid.consul.v1.agent.model.NewService;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class ConsulInstanceConverter extends NewService implements Converter<ConsulInstanceConverter> {

    private  NewService newService;


    public  ConsulInstanceConverter(){
        this.newService = new NewService();
    }
    public ConsulInstanceConverter(NewService newService){

        this.newService = new NewService();
        ConsulInstanceConverter converter = this.createObject(ConsulInstanceConverter.class, newService);
        try {
            BeanUtils.copyProperties(this,converter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        this.newService = newService;
    }
    public  ConsulInstanceConverter(StandardServiceMetadata metadata){

    }

    @Override
    public StandardServiceMetadata converter() {
        return null;
    }

    @Override
    public ConsulInstanceConverter convertT(StandardServiceMetadata metadata) {
        return null;
    }

    protected <T> T createObject(Class<T> clazz,Object source){

        String json = JSON.toJSONString(source);
        T t = JSON.parseObject(json, clazz);
        return  t;
    }
}
