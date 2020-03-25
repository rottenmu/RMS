package com.register.move.service.core.register;

import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.ServiceMetaData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZookeeperRegisteredServiceMetaData extends ServiceMetaData {

    private Map<String,Object>  metadata = new ConcurrentHashMap<>();



    @Override
    public Class<?> getInstanceClass() {
        return null;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.ZOOKEEPER;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
