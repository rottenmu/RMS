package com.register.move.service.mp.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "rms.plugin" )
@Slf4j
public class RegisterMoveConfig {

    private String sourceServerAddr;

    private String destinationServerAddr;

    private String sourceRegisterType;

    private String destinationRegisterType;

    private List<String> serviceNames;

    /**
     * example [app0,app1]
     */
    private String serviceNameStr;


    private  String destDriverClass ;
    private  String sourceDriverClass;
    private  String region;
    private  String group;
    public static void main(String[] args) {

    }

    public List<String> getServiceNames() {
        if(!this.serviceNameStr.contains(",")){
            return  Arrays.asList(this.serviceNameStr);
        }
        this.serviceNames = Arrays.stream(this.serviceNameStr.split(",")).collect(Collectors.toList());
        return serviceNames;
    }


    public String getServiceNameStr() {
        return serviceNameStr;
    }

    public void setServiceNameStr(String serviceNameStr) {
        this.serviceNameStr = serviceNameStr;
    }

    public void setSourceRegisterType(String sourceRegisterType) {
        this.sourceRegisterType = sourceRegisterType;
    }

    public String getSourceRegisterType() {
        return sourceRegisterType;
    }

    public void setSourceServerAddr(String sourceServerAddr) {
        this.sourceServerAddr = sourceServerAddr;
    }

    public String getSourceServerAddr() {
        return sourceServerAddr;
    }

    public void setDestinationServerAddr(String destinationServerAddr) {
        this.destinationServerAddr = destinationServerAddr;
    }

    public String getDestinationServerAddr() {
        return destinationServerAddr;
    }

    public void setDestinationRegisterType(String destinationRegisterType) {

//        if (Objects.equals(RegisterType.EUREKA.getDesc().toUpperCase(),destinationRegisterType)) {
//                log.error("destinationRegisterType can not be {}",destinationRegisterType);
//                try {
//                    throw new RegistryMoveException(RegistryMoveException.INVALID_PARAM,"destinationRegisterType can not be ::::> "+destinationRegisterType);
//                } catch (RegistryMoveException e) {
//                    e.printStackTrace();
//                    Runtime.getRuntime().halt(-1);
//                }
//        }

        this.destinationRegisterType = destinationRegisterType;
    }

    public String getDestinationRegisterType() {
        return destinationRegisterType;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }


    public void setDestDriverClass(String destDriverClass) {
        this.destDriverClass = destDriverClass;
    }

    public String getDestDriverClass() {
        return destDriverClass;
    }


    public String getSourceDriverClass() {
        return sourceDriverClass;
    }

    public void setSourceDriverClass(String sourceDriverClass) {
        this.sourceDriverClass = sourceDriverClass;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
