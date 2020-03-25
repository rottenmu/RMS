package com.register.move.service.plugin.nacos.utils;

import com.alibaba.nacos.client.naming.NacosNamingService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class NacosUtils {

    public  static String getFieldVal(NacosNamingService service,String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = service.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return String.valueOf(field.get(service));
    }

    public  static  String dataId(String ip,String serviceName,Integer port){

        return  serviceName.concat("-").concat("client-").concat(ip).concat(":").concat(port.toString());
    }

    public  static   String transformName(String serviceName,String group){
        int index = serviceName.indexOf("@@")+"@@".length();
        String name = StringUtils.substring(serviceName, index, serviceName.length());
        return serviceName.contains("@@")?name:serviceName;
    }
}
