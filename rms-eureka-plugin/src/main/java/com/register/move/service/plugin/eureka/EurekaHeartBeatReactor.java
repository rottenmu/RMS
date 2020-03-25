package com.register.move.service.plugin.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public  class EurekaHeartBeatReactor {

    private   volatile  Long clientBeatInterval = 5 * 1000L;
    protected ScheduledExecutorService executorService;
    protected final Map<String, InstanceInfo> eurekaBeat = new ConcurrentHashMap<>();

    public EurekaHeartBeatReactor(){
        executorService = new ScheduledThreadPoolExecutor(4, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(EurekaHeartBeatReactor.class.getName());
            return thread;
        });
        executorService.schedule(new BeatProcessor(), 0, TimeUnit.SECONDS);
    }

    public void addInstance(String key, InstanceInfo value) {
        this.eurekaBeat.put(key, value);
    }

    public void removeInstance(String key) {
        log.debug("[BEAT] removing beat: {} to beat map.", key);
        this.eurekaBeat.remove(key);
    }

    class  BeatProcessor implements Runnable{
        EurekaHttpClient eurekaHttpClient = (EurekaHttpClient) RegisterCache.getCache().get(RegisterType.EUREKA);

        @Override
        public void run() {
            try {
                for (Map.Entry<String, InstanceInfo> entry :eurekaBeat.entrySet()) {
                    InstanceInfo instanceInfo = entry.getValue();
                    executorService.schedule(() -> {
                        log.debug("[BEAT] adding beat: {} to beat map.", instanceInfo.getId());
                        eurekaHttpClient.sendHeartBeat(instanceInfo.getAppName(),
                                instanceInfo.getId(), instanceInfo, InstanceInfo.InstanceStatus.UP);
                    }, 0, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                log.error("[CLIENT-BEAT] Exception while scheduling beat.", e);
            }finally {
                executorService.schedule(this, clientBeatInterval, TimeUnit.MILLISECONDS);
            }
        }
    }



}
