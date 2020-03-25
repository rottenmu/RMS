package com.register.move.service.core.components;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimingTaskService  implements  Runnable{

    protected ScheduledExecutorService executorService;
    private volatile Long clientBeatInterval = 5 * 1000L;

    public TimingTaskService(Long clientBeatInterval) {

        if(Objects.equals(0,clientBeatInterval) || Objects.equals(null,clientBeatInterval)){
           clientBeatInterval = this.clientBeatInterval;
        }
        executorService = new ScheduledThreadPoolExecutor(4, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName(TimingTaskService.class.getName());
            return thread;
        });
        executorService.schedule(() ->{}, clientBeatInterval, TimeUnit.SECONDS);
    }

    @Override
    public void run() {

    }
}