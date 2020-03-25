package com.register.move.service.plugin.eureka;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        try {
            ConfigService configService = NacosFactory.createConfigService("106.13.201.243:8848");
            boolean bool = configService.publishConfig("127.0.0.1:8080@default", "default",
                    JSON.toJSONString("hello world"));
            if (bool) {
                System.out.println("hello");
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
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

//        RegisterClientConfig registerConfig = new RegisterClientConfig();
//        registerConfig.setServerAddr("http://127.0.0.1:7003/eureka/");
//        try {
//            EurekaNamingService eurekaNamingService = new EurekaNamingService();
//            eurekaNamingService.init(registerConfig);
//            EurekaHttpClient client = (EurekaHttpClient) RegisterCache.getCache().get(RegisterType.EUREKA);
//            EurekaHttpResponse<Application> applicationsEntity = client.getApplication("eureka0");
//            if (Objects.nonNull(applicationsEntity)) {
//                System.out.println(applicationsEntity.getEntity().getInstances());
//                List<InstanceInfo> instances = applicationsEntity.getEntity().getInstances();
//                Map<String, InstanceInfo> collect = instances.stream().collect(Collectors.toMap(InstanceInfo::getId, a -> a, (k1, k2) -> k1));
//                EurekaHeartBeatReactor reactor = new EurekaHeartBeatReactor("demo",collect);
//                for (InstanceInfo instance : instances) {
//                    EurekaHttpResponse<InstanceInfo> instanceInfoEurekaHttpResponse = client.sendHeartBeat(instance.getAppName(),
//                            instance.getId(), instance, InstanceInfo.InstanceStatus.UP);
//                    System.out.println(instanceInfoEurekaHttpResponse);
//                }
//
//            }
//            System.out.println(client);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
