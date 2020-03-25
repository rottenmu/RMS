package com.register.move.service.plugin.nacos;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.beat.BeatInfo;
import com.alibaba.nacos.client.naming.beat.BeatReactor;
import com.alibaba.nacos.client.naming.net.NamingProxy;
import com.register.move.service.plugin.nacos.utils.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NacosHeartBeatReactor  {

    private   volatile  Long clientBeatInterval = 5 * 1000L;
    protected ScheduledExecutorService executorService;
    protected final Map<String, Instance> nacosBeat = new ConcurrentHashMap<>();
    protected NamingProxy serverProxy;
    public NacosHeartBeatReactor(NamingProxy serverProxy){

        this.serverProxy =serverProxy;
        executorService = new ScheduledThreadPoolExecutor(30, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(NacosHeartBeatReactor.class.getName());
            return thread;
        });
    }

    public void addInstance(String key, Instance value) {
        this.nacosBeat.put(key, value);
    }

    public void removeInstance(String key) {
        log.debug("[BEAT] removing beat: {} to beat map.", key);
        this.nacosBeat.remove(key);
    }

    public static void main(String[] args) throws NoSuchFieldException {
        Properties properties = new Properties();
        properties.put("serverAddr","106.13.201.243");
        NacosNamingService service = new NacosNamingService(properties);
        BeatReactor beatReactor = service.getBeatReactor();
        try {
            String namespace = NacosUtils.getFieldVal(service, "namespace");
            String endpoint = NacosUtils.getFieldVal(service, "endpoint");
            NamingProxy proxy = new NamingProxy(namespace,endpoint,String.valueOf(properties.get("serverAddr")));
            BeatInfo beatInfo  = new BeatInfo();
            beatInfo.setIp("192.168.43.42");
            beatInfo.setPort(3004);
            beatInfo.setServiceName("eureka0");
            beatInfo.setWeight(1.0D);
            long l = proxy.sendBeat(beatInfo);
            System.out.println(l);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    class  BeatTask implements Runnable{

        private  BeatInfo beatInfo;
        public BeatTask(BeatInfo beatInfo){
            this.beatInfo = beatInfo;
        }
        @Override
        public void run() {
            if (beatInfo.isStopped()) {
                return;
            }
            long result = serverProxy.sendBeat(beatInfo);
            long nextTime = result > 0 ? result : beatInfo.getPeriod();
            try {
                for (Map.Entry<String, Instance> entry :nacosBeat.entrySet()) {
                    Instance instance = entry.getValue();
                    executorService.schedule(() -> {
                        log.debug("[BEAT] adding beat: {} to beat map.", instance.getInstanceId());
                    }, 0, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                log.error("[CLIENT-BEAT] Exception while scheduling beat.", e);
            }finally {
                executorService.schedule(new BeatTask(beatInfo), nextTime, TimeUnit.MILLISECONDS);
            }
        }
    }

}
