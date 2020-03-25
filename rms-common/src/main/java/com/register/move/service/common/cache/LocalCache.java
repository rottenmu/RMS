package com.register.move.service.common.cache;


import com.register.move.service.common.constants.RegisterType;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCache extends ConcurrentHashMap<String,Object> {

    private   volatile  static LocalCache CACHE  = new LocalCache();

    public  static synchronized LocalCache getCache(){

        if (Objects.isNull(CACHE)){
            CACHE = new LocalCache();
            return CACHE;
        }else{
            return  CACHE;
        }
    }
    public  void  set(String type, Object client){

        CACHE.set(type,client);
    }

    public  void   remove(RegisterType type){
        CACHE.remove(type);
    }

}
