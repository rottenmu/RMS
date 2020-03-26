package com.register.move.service.core.components;


import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.appinfo.InstanceInfo;
import com.register.move.service.domain.StandardServiceMetadata;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

public class ServiceMetadataConverter {

    public static ListIterator<StandardServiceMetadata> converterStandard(Iterable<InstanceInfo> infoIterable){

        List<StandardServiceMetadata> metadataList = new ArrayList<>();
        infoIterable.forEach(instanceInfo -> {
            StandardServiceMetadata metadata = new StandardServiceMetadata();
            metadata.setId(instanceInfo.getId());
            metadata.setIp(instanceInfo.getIPAddr());
            metadata.setPort(instanceInfo.getPort());
            metadata.setServiceName(instanceInfo.getAppName().toLowerCase());
            metadata.setHealthy(Objects.equals("UP",instanceInfo.getStatus().name())?true:false);
            metadata.setExtendedAttributes(instanceInfo.getMetadata());
            metadataList.add(metadata);
        });
        return  metadataList.listIterator();
    }

    public  static Stream<Instance> convrterNacosData(List<StandardServiceMetadata> metadataList){

        List<Instance> instanceList = new ArrayList<>(metadataList.size());
        try {
            BeanUtils.copyProperties(instanceList,metadataList);
            return instanceList.stream();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
            return  null;
    }

}
