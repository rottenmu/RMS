package com.register.move.service.plugin.zookeeper.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ZookeeperServiceInstances implements Iterable<ServiceInstance<ZookeeperInstance>> {


    private static final Log log = LogFactory.getLog(ZookeeperServiceInstances.class);

    private ServiceDiscovery<ZookeeperInstance> serviceDiscovery;
    private final List<ServiceInstance<ZookeeperInstance>> allInstances;
    private final CuratorFramework curator;

    public ZookeeperServiceInstances(CuratorFramework curator,
                                     ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        this.curator = curator;
        this.serviceDiscovery = serviceDiscovery;
        this.allInstances = getZookeeperInstances();
    }

    private List<ServiceInstance<ZookeeperInstance>> getZookeeperInstances() {
        ArrayList<ServiceInstance<ZookeeperInstance>> allInstances = new ArrayList<>();
        return  null;
    }

    private ServiceDiscovery<ZookeeperInstance> getServiceDiscovery() {
        return this.serviceDiscovery;
    }


    private List<ServiceInstance<ZookeeperInstance>> injectZookeeperServiceInstances(
            List<ServiceInstance<ZookeeperInstance>> accumulator, String name)
            throws Exception {
        Collection<ServiceInstance<ZookeeperInstance>> instances = getServiceDiscovery().queryForInstances(name);
        accumulator.addAll(convertCollectionToList(instances));
        return accumulator;
    }

    private List<ServiceInstance<ZookeeperInstance>> convertCollectionToList(
            Collection<ServiceInstance<ZookeeperInstance>> instances) {
        List<ServiceInstance<ZookeeperInstance>> serviceInstances = new ArrayList<>();
        for (ServiceInstance<ZookeeperInstance> instance : instances) {
            serviceInstances.add(instance);
        }
        return serviceInstances;
    }

    private List<ServiceInstance<ZookeeperInstance>> iterateOverChildren(
            List<ServiceInstance<ZookeeperInstance>> accumulator, String parentPath,
            List<String> children) throws Exception {
        List<ServiceInstance<ZookeeperInstance>> lists = new ArrayList<>();
        for (String child : children) {
            lists.addAll(null);
        }
        return lists;
    }


    @Override
    public Iterator<ServiceInstance<ZookeeperInstance>> iterator() {
        return this.allInstances.iterator();
    }
}
