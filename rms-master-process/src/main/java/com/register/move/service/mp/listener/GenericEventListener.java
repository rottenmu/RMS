package com.register.move.service.mp.listener;

import com.register.move.service.event.BaseEvent;
import com.register.move.service.listener.EventListener;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GenericEventListener implements EventListener<BaseEvent> {


    @Override
    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {

        System.out.println(event);
    }
}
