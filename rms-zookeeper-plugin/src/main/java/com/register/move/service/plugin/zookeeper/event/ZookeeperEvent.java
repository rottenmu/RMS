package com.register.move.service.plugin.zookeeper.event;

import com.register.move.service.event.BaseEvent;
import com.register.move.service.plugin.zookeeper.domain.ZookeeperInstance;
import lombok.Data;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ZookeeperEvent extends BaseEvent {

    private String group;
    private CopyOnWriteArrayList<ServiceInstance<ZookeeperInstance>> serviceInstances = new CopyOnWriteArrayList<>();
}
