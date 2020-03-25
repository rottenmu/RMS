package com.register.move.service.mp.callbak;

import com.register.move.service.listener.EventListener;
import com.register.move.service.mp.event.AppEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StopAppEventListener implements EventListener<AppEvent> {

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        Runtime.getRuntime().halt(event.getExitCode());
    }
}
