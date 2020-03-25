package com.register.move.service.event;

import com.register.move.service.core.ZookeeperInstance;
import lombok.Data;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ZookeeperEvent extends BaseEvent {

    private String group;
    private CopyOnWriteArrayList<ServiceInstance<ZookeeperInstance>> serviceInstances = new CopyOnWriteArrayList<>();
}
