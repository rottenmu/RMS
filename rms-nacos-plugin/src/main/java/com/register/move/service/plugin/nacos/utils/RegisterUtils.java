package com.register.move.service.plugin.nacos.utils;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

public class RegisterUtils {



    public  static NamingService getNameingService(String serverList){

        try {
            NamingService namingService = NamingFactory.createNamingService(serverList);
            return namingService;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
