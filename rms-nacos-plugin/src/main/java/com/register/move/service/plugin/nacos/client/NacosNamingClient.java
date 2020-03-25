package com.register.move.service.plugin.nacos.client;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.config.RegisterClientConfig;
import com.register.move.service.plugin.nacos.NacosHeartBeatReactor;
import com.register.move.service.plugin.nacos.utils.RegisterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class NacosNamingClient implements RegisterClient<Instance, NamingService> {

    private  final ConcurrentMap<RegisterType,Object> registerMap =  RegisterCache.getCache();
    private  final ConcurrentMap<RegisterType,Object> REGMAP =  new ConcurrentHashMap<>();
    private  NamingService namingService;
    private RegisterClientConfig registerClientConfig;
    private NacosHeartBeatReactor beatReactor;

    /**
     *
     */
    public NacosNamingClient(){
        try {
            namingService = init(registerClientConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public NacosNamingClient(RegisterClientConfig registerClientConfig){
        try {
            namingService = init(registerClientConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public NamingService init(RegisterClientConfig registerConfig) throws Exception {

        if (Objects.nonNull(registerMap.get(RegisterType.NACOS))){
            return ((NamingService) registerMap.get(RegisterType.NACOS));
        }
        NamingService nameingService = RegisterUtils.getNameingService(registerConfig.getServerAddr());
        RegisterCache.getCache().put(RegisterType.NACOS,nameingService);
        this.namingService = nameingService;
        return  nameingService;
    }

    @Override
    public void registerInstance(Instance instance) {
//        NamingService namingService = (NamingService) registerMap.get(RegisterType.NACOS);
        try {
            namingService.registerInstance(instance.getServiceName(),instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deregisterInstance(Instance instance) {
        //      NamingService namingService = (NamingService) registerMap.get(RegisterType.NACOS);
        try {
            namingService.registerInstance(instance.getServiceName(),instance);
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }


    @Override
    public List<Instance> serviceInstanceInfo(String serviceName, String region) {

        List<Instance> allInstances = new ArrayList<>();
        try {
            allInstances = namingService.getAllInstances(serviceName);
            allInstances.forEach(instance -> {
                instance.setServiceName(transformName(instance.getServiceName(),Objects.nonNull(region)?region:GROUP));
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return allInstances;
    }

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.NACOS;
    }

    @Override
    public RegisterClient<Instance, NamingService> setRegister(NamingService service) {

//        if (Objects.nonNull(registerMap.get(getRegisterType()))) {
//            return this;
//        }else {
//            REGMAP.put(getRegisterType(),client);
//        }
        return this;
    }
    protected  void  setClient(){
        registerMap.put(RegisterType.NACOS,this);
    }

    protected  String transformName(String serviceName,String group){
        int index = serviceName.indexOf("@@")+"@@".length();
        String name = StringUtils.substring(serviceName, index, serviceName.length());
        return  name;
    }
}
