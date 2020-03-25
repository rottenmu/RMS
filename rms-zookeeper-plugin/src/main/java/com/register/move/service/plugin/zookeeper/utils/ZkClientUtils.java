package com.register.move.service.plugin.zookeeper.utils;

import com.google.gson.Gson;
import com.register.move.service.core.register.ZookeeperRegisteredServiceMetaData;
import com.register.move.service.plugin.zookeeper.domain.ZookeeperInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ZkClientUtils {

    private final   static  ThreadLocal<CuratorFramework>  ZKCLINT = new ThreadLocal<>();
    private  final static String BASEFILE = "/services";

    public  static  List<ServiceInstance<ZookeeperInstance>>  allServiceMetaData (CuratorFramework zkClient,Iterable<String> iterable) throws Exception {

        GetChildrenBuilder builder = zkClient.getChildren();
        List<String> pathList = pathList(builder, zkClient,iterable);
        List<ServiceInstance<ZookeeperInstance>> serviceMetaDatas = new ArrayList<>(pathList.size());
        Gson gson = new Gson();
        pathList.forEach(p ->{
            try {
                String metadata = new String(zkClient.getData().forPath(p));
                ServiceInstance<ZookeeperInstance> serviceInstance = gson.fromJson(metadata, ServiceInstance.class);
                ZookeeperRegisteredServiceMetaData serviceMetaData = new ZookeeperRegisteredServiceMetaData();
                serviceMetaData.setIp(serviceInstance.getAddress());
                serviceMetaData.setPort(serviceInstance.getPort());
                serviceMetaData.setServiceName(serviceInstance.getName());
                serviceMetaDatas.add(serviceInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return  serviceMetaDatas;
    }

    /**
     *  get all path
     * @param builder
     * @param zkClient
     * @return
     * @throws Exception
     */
    public  static  List<String> pathList(GetChildrenBuilder builder,CuratorFramework zkClient,Iterable<String> iterable) throws Exception {
        List<String> childList = builder.forPath(BASEFILE);
        List<String> pathList = new ArrayList<>(childList.size());
        childList.stream().forEach(path ->{
            try {
                String makePath = null;
                //!iterable.iterator().hasNext()
                if (iterable.iterator().hasNext()) {
                    makePath = ZKPaths.makePath(BASEFILE, path);
                }else {
                    iterable.forEach(i ->{

                    });
                }
                List<String> path0 = zkClient.getChildren().forPath(makePath);
                if (CollectionUtils.isNotEmpty(path0)){
                    pathList.add(makePath.concat("/").concat(path0.get(0)));
                }else {
                    log.error("this {} :::not registered on the zookeeper server",path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pathList.forEach(p ->{
            try {
                System.out.println(new String(zkClient.getData().forPath(p)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return  pathList;

    }
    public  static  Map<String,Object>  serviceMetaData (CuratorFramework zkClient,String serviceName) throws Exception {
        Gson gson = new Gson();
        String path = ZKPaths.makePath(BASEFILE, serviceName);
        List<String> strings = zkClient.getChildren().forPath(path);
        String substring = StringUtils.substring(strings.get(0), 0, strings.get(0).length());
        byte[] bytes = zkClient.getData().forPath(path + "/" + substring);
        return gson.fromJson(gson.toJson(String.valueOf(bytes)),Map.class);
    }

    public  static CuratorFramework  getInstance(String serverAddr){
        if (Objects.isNull(ZKCLINT.get())) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
            CuratorFramework zkClient = CuratorFrameworkFactory.newClient(serverAddr,retryPolicy);
            zkClient.start();
            return  zkClient;
        }else{
            return  ZKCLINT.get();
        }
    }

}
