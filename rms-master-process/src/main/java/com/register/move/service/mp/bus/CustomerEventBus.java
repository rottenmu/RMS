package com.register.move.service.mp.bus;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class CustomerEventBus extends EventBus {

    private  volatile static CustomerEventBus  CUSTOMEREVENTBUS = new CustomerEventBus();

    public synchronized   static CustomerEventBus getEventBus (Object ...observer ){

        if (Objects.isNull(CUSTOMEREVENTBUS)) {
            CUSTOMEREVENTBUS = new CustomerEventBus();
            for (Object o : observer) {
                CUSTOMEREVENTBUS.register(o);
            }
            return  CUSTOMEREVENTBUS;
        }else {
            CUSTOMEREVENTBUS = new CustomerEventBus();
            for (Object o : observer) {
                CUSTOMEREVENTBUS.register(o);
            }
            return  CUSTOMEREVENTBUS;
        }
    }
}
