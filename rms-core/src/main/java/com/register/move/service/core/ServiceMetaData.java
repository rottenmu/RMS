package com.register.move.service.core;

import com.register.move.service.common.constants.RegisterType;

import java.util.Map;

public  abstract class ServiceMetaData   {
	private String serviceName ;

	private String ip;

	private Integer port;

	private String group;

	public abstract Class<?>  getInstanceClass();

	public abstract Map<String,Object> getMetadata();

	public abstract RegisterType getRegisterType();


	public void setGroup(String group) {
		this.group = group;
	}

	public Integer getPort() {
		return port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getGroup() {
		return group;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}
}
