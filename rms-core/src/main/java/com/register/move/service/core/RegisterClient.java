package com.register.move.service.core;

import com.register.move.service.common.cache.LocalCache;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.config.RegisterClientConfig;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public interface RegisterClient<T,R> {

    String GROUP = "DEFAULT_GROUP";

    ConcurrentHashMap<String,Object> CACHE = LocalCache.getCache();

    ConcurrentMap<RegisterType,Object> REGISTERMAP =  RegisterCache.getCache();

    R init(RegisterClientConfig registerConfig) throws Exception;

    void registerInstance(T t);

    void deregisterInstance(T t);

    List<T> serviceInstanceInfo(String serviceName, String region) throws Exception;

    RegisterType getRegisterType();

    @Deprecated
    RegisterClient<T,R> setRegister(R r);




}
