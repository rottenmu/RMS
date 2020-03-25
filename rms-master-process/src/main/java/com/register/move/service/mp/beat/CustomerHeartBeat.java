package com.register.move.service.mp.beat;

import com.register.move.service.common.cache.LocalCache;
import com.register.move.service.listener.EventListener;
import com.register.move.service.mp.event.BeatEvent;
import com.register.move.service.plugin.eureka.EurekaHeartBeatReactor;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.springframework.stereotype.Component;

@Component
public class CustomerHeartBeat extends EurekaHeartBeatReactor implements EventListener<BeatEvent> {

    protected LocalCache cache = LocalCache.getCache();

    public CustomerHeartBeat(){

        this.set();
    }
    @Override
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(BeatEvent event) {
        eurekaBeat.remove(event.getKey());
    }

    private  void set(){

        cache.putAll(eurekaBeat);
    }
}
