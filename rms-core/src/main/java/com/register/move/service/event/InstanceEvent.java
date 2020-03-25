package com.register.move.service.event;


import com.alibaba.fastjson.JSON;
import com.register.move.service.common.constants.RegisterType;

import java.util.HashMap;
import java.util.Map;


/**
 *  服务注册事件
 */
public class InstanceEvent {

    private  String id;
    private  String serverAddr;
    /**
     * instance ip
     */
    private String ip;

    /**
     * instance port
     */
    private int port;

    /**
     * instance weight
     */
    private double weight = 1.0D;

    /**
     * instance health status
     */
    private boolean healthy = true;

    /**
     * If instance is enabled to accept request
     */
    private boolean enabled = true;

    /**
     * If instance is ephemeral
     *
     * @since 1.0.0
     */
    private boolean ephemeral = true;

    /**
     * cluster information of instance
     */
    private String clusterName;

    /**
     * Service information of instance
     */
    private String serviceName;

    /**
     * user extended attributes
     */
    private Map<String, String> metadata = new HashMap<String, String>();


    private RegisterType registerType;

    private  String Status;
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    private long getMetaDataByKeyWithDefault(String key, long defaultValue) {
        if (getMetadata() == null || getMetadata().isEmpty()) {
            return defaultValue;
        }
        return defaultValue;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
