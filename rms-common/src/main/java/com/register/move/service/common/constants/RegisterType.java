package com.register.move.service.common.constants;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum RegisterType {

    ZOOKEEPER(0,"zookeeper"),
    EUREKA(1,"eureka"),
    CONSUL(2,"consul"),
    NACOS(3,"nacos");

    private Integer num;
    private  String desc;

     RegisterType(Integer num,String desc){

         this.num = num;
         this.desc = desc;
     }

    public static ConcurrentMap<String, RegisterType> typeMap = new ConcurrentHashMap<String, RegisterType>();

    static {
        RegisterType[] types = RegisterType.values();
        for (RegisterType type : types) {
            typeMap.put(String.valueOf(type.desc), type);
        }
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getNum() {
        return num;
    }

    public String getDesc() {
        return desc;
    }
}
