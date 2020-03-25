package com.register.move.service.mp.event;

public class BeatEvent {

    /**
     *  服务心跳缓存的key
     */
    private  String key;

    public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
}
