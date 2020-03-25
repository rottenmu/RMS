package com.register.move.service.mp.utils;

import org.apache.commons.lang3.StringUtils;


public class MPUtils {


    public  static  String dataId(String ip,String serviceName,Integer port){

        return  serviceName.concat("-").concat("client-").concat(ip).concat(":").concat(port.toString());
    }

    public  static   String transformName(String serviceName,String group){
        int index = serviceName.indexOf("@@")+"@@".length();
        String name = StringUtils.substring(serviceName, index, serviceName.length());
        return serviceName.contains("@@")?name:serviceName;
    }
}
