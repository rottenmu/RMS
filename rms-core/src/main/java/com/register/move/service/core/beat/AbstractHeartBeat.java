package com.register.move.service.core.beat;
import com.alibaba.nacos.api.common.Constants;
import com.register.move.service.domain.Converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHeartBeat<T> {

    protected  volatile  Long clientBeatInterval = 5 * 1000L;
    protected ScheduledExecutorService executorService;
    protected final Map<String, Converter<T>> beat = new ConcurrentHashMap<>();


    public AbstractHeartBeat( Map<String, Converter<T>> beat,String threadName){
        this.beat.putAll(beat);
        executorService = new ScheduledThreadPoolExecutor(4, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(threadName);
            return thread;
        });
        executorService.schedule(this::beatThread, 0, TimeUnit.SECONDS);
    }
    protected abstract  Runnable beatThread();

    private String buildKey(String serviceName, String ip, int port) {
        return serviceName + Constants.NAMING_INSTANCE_ID_SPLITTER
                + ip + Constants.NAMING_INSTANCE_ID_SPLITTER + port;
    }
}
