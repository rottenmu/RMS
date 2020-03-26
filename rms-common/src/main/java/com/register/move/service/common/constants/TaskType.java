package com.register.move.service.common.constants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum TaskType {

    ZKTASK(RegisterType.ZOOKEEPER,"com.register.move.service.plugin.zookeeper.listener.ZookeeperTask"),
    EUREKATASK(RegisterType.EUREKA,"com.register.move.service.plugin.eureka.task.EurekaTask"),
    CONSULTASK(RegisterType.CONSUL,"com.register.move.service.plugin.consul.domain.ConsulTask"),
    NACOSTASK(RegisterType.NACOS,"com.register.move.service.plugin.nacos.task.NacosTask");

    private RegisterType registerType;
    private  String className;

    TaskType(RegisterType registerType, String className){

        this.registerType = registerType;
        this.className = className;
    }

    public static ConcurrentMap<String, TaskType> taskTypeMap = new ConcurrentHashMap<>(1<<2);

    static {
        TaskType[] types = TaskType.values();
        for (TaskType type : types) {
            taskTypeMap.put(String.valueOf(type.registerType.getDesc()), type);
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
