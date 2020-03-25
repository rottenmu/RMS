package com.register.move.service.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class StandardServiceMetadata implements Serializable {

    private  String id;
    private  String serviceName;
    private  String ip;
    private  String hostName;
    private  Integer port;
    private  Double weight = 1.0D;
    private  Boolean  healthy;
    private  String clusterName;
    private Map<String,String> extendedAttributes;
    private  String group;
}
