package com.register.move.service.listener;

import com.register.move.service.event.BaseEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GenericEventListener implements EventListener<BaseEvent> {
    @Override
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(BaseEvent event) {
        event.getRunnable().run();
    }
}
