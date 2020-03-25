package com.register.move.service.plugin.eureka;

public class BeatReactor {
    public static void main(String[] args) {

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
//               EurekaHeartBeatReactor reactor = new EurekaHeartBeatReactor("demo",collect);
////                for (InstanceInfo instance : instances) {
////                    EurekaHttpResponse<InstanceInfo> instanceInfoEurekaHttpResponse = client.sendHeartBeat(instance.getAppName(),
////                            instance.getId(), instance, InstanceInfo.InstanceStatus.UP);
////                    System.out.println(instanceInfoEurekaHttpResponse);
////                }
//
//            }
//            System.out.println(client);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
