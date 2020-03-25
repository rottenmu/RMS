package com.register.move.service.common.constants;

public enum  EventType {



    REG (0,"register"),
    CANCEL(1,"cancel"),
    BEAT (2,"beat"),
    SEARCH (3,"search");

    private  Integer code;
    private  String desc;

    EventType(Integer code,String desc){

        this.code = code;
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}