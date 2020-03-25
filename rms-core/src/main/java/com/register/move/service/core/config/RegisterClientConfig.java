package com.register.move.service.core.config;

import com.register.move.service.common.constants.EventType;
import com.register.move.service.common.constants.RegisterType;

import java.util.Map;

public class RegisterClientConfig {
    private  String serverAddr;
    private RegisterType registerType;
    private  Map<String,Object> metadata;
    private  String className;
    private EventType eventType;
    private  String address;
    private  Integer port;
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
