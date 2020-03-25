package com.register.move.service.common.constants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ConverterType {

    ZOOKEEPER(RegisterType.ZOOKEEPER,"com.register.move.service.plugin.zookeeper.domain.ZookeeperInstanceConverter"),
    EUREKA(RegisterType.EUREKA,"com.register.move.service.plugin.eureka.domain.EurekaInstanceConverter"),
    CONSUL(RegisterType.CONSUL,"com.register.move.plugin.domain.InstanceUseForConsul"),
    NACOS(RegisterType.NACOS,"com.register.move.service.plugin.nacos.domain.NacosInstanceConverter");

    private RegisterType registerType;
    private  String className;

    ConverterType(RegisterType registerType, String className){

        this.registerType = registerType;
        this.className = className;
    }

    public static ConcurrentMap<String, ConverterType> converterMap = new ConcurrentHashMap<String, ConverterType>();
    static {
        ConverterType[] types = ConverterType.values();
        for (ConverterType type : types) {
            converterMap.put(String.valueOf(type.registerType.getDesc()), type);
        }
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }
}
