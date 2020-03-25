package com.register.move.service.event;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.appinfo.MyDataCenterInfo;
import com.register.move.service.domain.StandardServiceMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Data
public class EurekaEvent  extends  BaseEvent{

    private static final String COLON = ":";
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String HTTP_PROTOCOL = "http://";
    private   String  relativeUrl =  "/actuator/health";
    private  long timeoutMs;
    private CopyOnWriteArrayList<InstanceInfo> instanceInfos = new CopyOnWriteArrayList<>();

    public  List<InstanceInfo>  transformObjects(){
        super.getMetaData().forEach(m ->{
        //    InstanceInfo instanceInfo = InstanceUseForEureka.createInstance(m.getServiceName(), m.getIp(), m.getPort(), m.getExtendedAttributes());
         //   instanceInfo.setStatus(m.getHealthy()? InstanceInfo.InstanceStatus.UP: InstanceInfo.InstanceStatus.DOWN);
            InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder().setAppName(m.getServiceName())
                    .setHostName(m.getIp()).setPort(m.getPort()).setMetadata(m.getExtendedAttributes())
                    .setVIPAddress(m.getIp()).setIPAddr(m.getServiceName()).setSecureVIPAddress(m.getServiceName())
                    .setHealthCheckUrls(relativeUrl, HTTP_PROTOCOL + m.getIp() + ":" + m.getPort() + relativeUrl, null)
                    .setActionType(InstanceInfo.ActionType.ADDED)
                    .setHomePageUrl(null,HTTP_PROTOCOL + m.getIp() + ":" + m.getPort())
                    .setOverriddenStatus(InstanceInfo.InstanceStatus.UP)
                    .setStatus(m.getHealthy() ? InstanceInfo.InstanceStatus.UP : InstanceInfo.InstanceStatus.DOWN)
                    .setLastDirtyTimestamp(System.currentTimeMillis())
                    .setLastUpdatedTimestamp(System.currentTimeMillis())
                    .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                    .setLeaseInfo(new LeaseInfo(30, 90,
                            0L, 0L, 0L, 0L, 0L))
                    .setInstanceId(m.getServiceName() + ":" + m.getPort()).setSecurePort(443).build();
            instanceInfos.add(instanceInfo);
        });
     return instanceInfos;
    }
    protected  void  set(InstanceInfo instanceInfo){

    }

    public InstanceInfo buildSyncInstance(StandardServiceMetadata instance, String group) {
        DataCenterInfo dataCenterInfo = new MyDataCenterInfo(DataCenterInfo.Name.MyOwn);
        HashMap<String, String> metadata = new HashMap<>(16);
        String homePageUrl = "http://" + instance.getIp() + ":" + instance.getPort()+"/";
        String serviceName = this.transformName(instance.getServiceName(),group);
        return new InstanceInfo(
                "LAPTOP-E9V666V4" + ":" + serviceName + ":" + instance.getPort(),
                serviceName,
                null,
                instance.getIp(),
                null,
                new InstanceInfo.PortWrapper(true, instance.getPort()),
                null,
                homePageUrl,
                homePageUrl + "actuator/info",
                homePageUrl + "actuator/health",
                null,
                serviceName,
                serviceName,
                1,
                dataCenterInfo,
                instance.getIp(),
                InstanceInfo.InstanceStatus.UP,
                InstanceInfo.InstanceStatus.UNKNOWN,
                null,
                new LeaseInfo(30, 90,
                        0L, 0L, 0L, 0L, 0L),
                false,
                metadata,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                InstanceInfo.ActionType.ADDED,
                null
        );
    }
   private    String transformName(String serviceName,String group){
        int index = serviceName.indexOf("@@")+"@@".length();
        String name = StringUtils.substring(serviceName, index, serviceName.length());
        return serviceName.contains("@@")?name:serviceName;
    }
}
