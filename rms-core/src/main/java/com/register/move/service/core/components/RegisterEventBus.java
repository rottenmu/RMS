package com.register.move.service.core.components;

import com.google.common.eventbus.EventBus;

import java.util.Objects;

public class RegisterEventBus extends EventBus {

    private  volatile static RegisterEventBus  EVENTBUS = new RegisterEventBus();

    public synchronized   static RegisterEventBus getEventBus (Object ...observer ){

        if (Objects.isNull(EVENTBUS)) {
            EVENTBUS = new RegisterEventBus();
            for (Object o : observer) {
                EVENTBUS.register(o);
            }
            return  EVENTBUS;
        }else {
            EVENTBUS = new RegisterEventBus();
            for (Object o : observer) {
                EVENTBUS.register(o);
            }
            return  EVENTBUS;
        }
    }

}
