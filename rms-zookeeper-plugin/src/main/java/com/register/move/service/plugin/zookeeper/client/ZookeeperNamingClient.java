package com.register.move.service.plugin.zookeeper.client;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.common.exception.RegistryMoveException;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.config.RegisterClientConfig;
import com.register.move.service.plugin.zookeeper.utils.ZkClientUtils;
import com.register.move.service.plugin.zookeeper.domain.ZookeeperInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ZookeeperNamingClient implements RegisterClient<ServiceInstance<ZookeeperInstance>, ServiceDiscovery<ZookeeperInstance>> {

    private  CuratorFramework zkClient;
    private   ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    public ZookeeperNamingClient(){

    }
    public ZookeeperNamingClient(RegisterClientConfig clientConfig){
        try {
            // deal with Packet len1213486160 is out of range! 1213486160
            System.setProperty("jute.maxbuffer", 204800 * 1024 * 10 + "");
            this.zkClient = ZkClientUtils.getInstance(clientConfig.getServerAddr());
            this.serviceDiscovery = init(clientConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public  ServiceDiscovery<ZookeeperInstance> init(RegisterClientConfig registerConfig) throws Exception {
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ZookeeperInstance.class)
                .basePath("/services")
                .client(this.zkClient)
                .build();
        serviceDiscovery.start();
        RegisterCache.getCache().put(getRegisterType(),serviceDiscovery);
        return serviceDiscovery;
    }


    public static void main(String[] args) {

        RegisterClientConfig clientConfig = new RegisterClientConfig();
        clientConfig.setServerAddr("106.13.201.243:2181");
        ZookeeperNamingClient namingClient = new ZookeeperNamingClient(clientConfig);
        try {
            List<ServiceInstance<ZookeeperInstance>> serviceInstances = namingClient.serviceInstanceInfo(null, null);
            System.out.println(serviceInstances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerInstance(ServiceInstance<ZookeeperInstance> serviceInstance) {

        try {
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deregisterInstance(ServiceInstance<ZookeeperInstance> serviceInstance) {
        try {
            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServiceInstance<ZookeeperInstance>> serviceInstanceInfo(String serviceName, String region) throws Exception {

   //     Collection<ServiceInstance<ZookeeperInstance>> serviceInstances1 = serviceDiscovery.queryForInstances(serviceName);
        List<ServiceInstance<ZookeeperInstance>> serviceInstances = ZkClientUtils.allServiceMetaData(zkClient,Arrays.asList(serviceName));
    //    List<ServiceInstance<ZookeeperInstance>>  serviceInstanceList = new ArrayList<>(serviceInstances1.size());
        if (CollectionUtils.isEmpty(serviceInstances)) {
            throw new RegistryMoveException(RegistryMoveException.NOT_EXIST,"serviceName ->"+serviceName+"Not registered on the zookeeper server");
        }
//        serviceInstanceList.addAll(serviceInstances1);
        return serviceInstances;
    }

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.ZOOKEEPER;
    }

    @Override
    public RegisterClient<ServiceInstance<ZookeeperInstance>,ServiceDiscovery<ZookeeperInstance>> setRegister(ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        return null;
    }

    private ServiceInstance buildServiceInstance(String address,int port,String serviceName){
        ServiceInstance<Object> serviceInstance = null;
        try {
            serviceInstance = ServiceInstance.builder().uriSpec(new UriSpec("{scheme}://{address}:{port}/"))
                    .address(address)
                    .port(port)
                    .name(serviceName)
                    .payload(new ZookeeperInstance())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceInstance;
    }

    private String id(ServiceInstance<ZookeeperInstance> serviceInstance){

        return serviceInstance.getAddress().concat(":"+serviceInstance.getPort()).concat(":").concat(serviceInstance.getName());
    }

}
