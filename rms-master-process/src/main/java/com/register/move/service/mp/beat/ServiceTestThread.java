package com.register.move.service.mp.beat;

import com.netflix.appinfo.InstanceInfo;
import com.register.move.service.event.InstanceEvent;
import com.register.move.service.mp.bus.CustomerEventBus;
import com.register.move.service.mp.event.BeatEvent;
import com.register.move.service.plugin.eureka.EurekaHeartBeatReactor;
import io.micrometer.core.ipc.http.HttpSender;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;


public class ServiceTestThread implements Callable<Map> {


    private   volatile  Long clientBeatInterval = 4 * 1000L;
    private InstanceEvent event;
    protected ScheduledExecutorService executorService;
    protected final Map<String, InstanceInfo> eurekaBeat = new ConcurrentHashMap<>();

    public ServiceTestThread(){
        executorService = new ScheduledThreadPoolExecutor(4, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(EurekaHeartBeatReactor.class.getName());
            return thread;
        });
        executorService.schedule(this::call, clientBeatInterval, TimeUnit.SECONDS);
    }


    @Override
    public Map call() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String buildUrl = buildUrl(event.getIp(), Integer.valueOf(event.getPort()));
        RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, URI.create(buildUrl));
        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(requestEntity, Map.class);
            Map body = responseEntity.getBody();
            String status = String.valueOf(body.get("status"));
            if (!Objects.equals("UP",status)) {
                //TODO 发布事件删除心跳信息
                BeatEvent event = new BeatEvent();
                event.setKey(this.event.getId());
                CustomerEventBus.getEventBus(new CustomerHeartBeat()).post(event);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    String buildUrl(String ip, Integer port) {
        StringBuilder builder = new StringBuilder("http://").append(ip)
                .append(":")
                .append(port.toString())
                .append("/actuator/health");
        return builder.toString();
    }

}
