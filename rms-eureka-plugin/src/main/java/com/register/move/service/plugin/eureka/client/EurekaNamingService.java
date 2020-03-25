package com.register.move.service.plugin.eureka.client;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.resolver.DefaultEndpoint;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.config.RegisterClientConfig;
import com.register.move.service.plugin.eureka.EurekaHeartBeatReactor;
import com.register.move.service.plugin.eureka.domain.EurekaInstanceConverter;
import org.springframework.cloud.netflix.eureka.http.RestTemplateTransportClientFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EurekaNamingService  implements RegisterClient<InstanceInfo, EurekaHttpClient> {

    protected  final   ConcurrentMap<RegisterType,Object> REGISTERMAP = new ConcurrentHashMap();
    private final static ThreadLocal<EurekaHeartBeatReactor>  BEATREACTOR = new ThreadLocal<>();
    protected EurekaHttpClient eurekaHttpClient;
    private EurekaHeartBeatReactor beatReactor;
    private  RegisterClientConfig clientConfig;
    public EurekaNamingService(RegisterClientConfig clientConfig){
        try {
            this.eurekaHttpClient = init(clientConfig);
            RegisterCache.getCache().put(getRegisterType(),eurekaHttpClient);
            beatReactor = new EurekaHeartBeatReactor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public EurekaNamingService(){
        try {
            this.eurekaHttpClient = init(clientConfig);
            RegisterCache.getCache().put(getRegisterType(),eurekaHttpClient);
            beatReactor = new EurekaHeartBeatReactor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private  final  static  String URI0 = "http://106.13.201.243:8761/eureka/apps/eureka0";
    private  final  static  String URI2 = "http://eureka.springcloud.cn/eureka/apps/";
    public static   void main(String[] args) throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Map> map = restTemplate.getForEntity(URI0, Map.class);
//        Object forEntity = ((Map) map.getBody().get("application")).get("instance");
//
//
//        RestTemplateTransportClientFactory restTemplateTransportClientFactory =
//                new RestTemplateTransportClientFactory();
//        EurekaEndpoint eurekaEndpoint = new DefaultEndpoint("http://127.0.0.1:7003/eureka/");
//        EurekaHttpClient eurekaHttpClient = restTemplateTransportClientFactory.newClient(eurekaEndpoint);
//        EurekaHttpResponse<Application> eureka0 = eurekaHttpClient.getApplication("eureka0");
//        eurekaHttpClient.getApplications("us-east-1");
//        System.out.println(eureka0.getEntity().getInstances());
//
//        System.out.println(forEntity);
        RegisterClientConfig registerConfig = new RegisterClientConfig();
        registerConfig.setServerAddr("http://127.0.0.1:7003/eureka/");
        try {
            EurekaNamingService eurekaNamingService = new EurekaNamingService(registerConfig);
            eurekaNamingService.init(registerConfig);
            EurekaHttpClient client = (EurekaHttpClient) RegisterCache.getCache().get(RegisterType.EUREKA);
            EurekaHttpResponse<Application> applicationsEntity = client.getApplication("eureka0");
            if (Objects.nonNull(applicationsEntity)) {
                System.out.println(applicationsEntity.getEntity().getInstances());

            }
            System.out.println(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public  EurekaHttpClient init(RegisterClientConfig registerConfig) throws Exception {
        RestTemplateTransportClientFactory restTemplateTransportClientFactory =
                new RestTemplateTransportClientFactory();
        EurekaEndpoint eurekaEndpoint = new DefaultEndpoint(registerConfig.getServerAddr());
        EurekaHttpClient eurekaHttpClient = restTemplateTransportClientFactory.newClient(eurekaEndpoint);
        return  eurekaHttpClient;
    }
    @Override
    public void registerInstance(InstanceInfo instanceInfo) {
        EurekaInstanceConverter instanceInfo2 = ((EurekaInstanceConverter) instanceInfo);
        EurekaHttpResponse<Void> response = eurekaHttpClient.register(instanceInfo2.getInstanceInfo());
        if (Objects.requireNonNull(HttpStatus.resolve(response.getStatusCode())).is2xxSuccessful()) {
            beatReactor.addInstance(instanceInfo.getId(), instanceInfo);
        }
    }

    @Override
    public void deregisterInstance(InstanceInfo instanceInfo) {
        EurekaHttpResponse<Void> response = eurekaHttpClient.cancel(instanceInfo.getAppName(), instanceInfo.getId());
        if (Objects.requireNonNull(HttpStatus.resolve(response.getStatusCode())).is2xxSuccessful()) {
            if (Objects.requireNonNull(HttpStatus.resolve(response.getStatusCode())).is2xxSuccessful()) {
                beatReactor.removeInstance(instanceInfo.getId());
            }
        }
    }

    @Override
    public List<InstanceInfo> serviceInstanceInfo(String serviceName, String region) {
        return eurekaHttpClient.getApplication(serviceName).getEntity().getInstances();
    }

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.EUREKA;
    }

    @Override
    public RegisterClient<InstanceInfo,EurekaHttpClient> setRegister(EurekaHttpClient eurekaHttpClient) {
        REGISTERMAP.put(RegisterType.EUREKA,eurekaHttpClient);
        return  this;
    }
    public InstanceInfo buildSyncInstance(Map instance) {
        DataCenterInfo dataCenterInfo = new MyDataCenterInfo(DataCenterInfo.Name.MyOwn);
        HashMap<String, String> metadata = new HashMap<>(16);
        metadata.putAll(instance);
        metadata.replace("homePageUrl","http://192.168.1.137:3004");
        metadata.replace("statusPageUrl","http://192.168.1.137:3004/actuator/info");
        metadata.replace("healthCheckUrl","http://192.168.1.137:3004/actuator/health");

        String serviceName = metadata.get("vipAddress");
        return new InstanceInfo(
                metadata.get("instanceId"),
                serviceName,
                null,
                metadata.get("ipAddr"),
                null,
                new InstanceInfo.PortWrapper(true, 3004),
                null,
                metadata.get("homePageUrl"),
                metadata.get("statusPageUrl"),
                metadata.get("healthCheckUrl"),
                null,
                serviceName,
                serviceName,
                1,
                dataCenterInfo,
                metadata.get("ipAddr"),
                InstanceInfo.InstanceStatus.UP,
                InstanceInfo.InstanceStatus.UNKNOWN,
                null,
                new LeaseInfo(30, 90,
                        0L, 0L, 0L, 0L, 0L),
                false,
                metadata,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                null,
                null
        );
    }

}
