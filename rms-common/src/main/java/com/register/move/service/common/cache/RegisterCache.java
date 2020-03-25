package com.register.move.service.common.cache;



import com.register.move.service.common.constants.RegisterType;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class RegisterCache extends  ConcurrentHashMap<RegisterType, Object> {

    private   volatile  static RegisterCache cache  = new RegisterCache();

    public  static synchronized RegisterCache getCache(){

        if (Objects.isNull(cache)){
            return  new RegisterCache();
        }else{
            return  cache;
        }
    }
    public  void  set(RegisterType type, Object client){

        cache.put(type,client);
    }

    public  void   remove(RegisterType type){

        cache.remove(type);

    }

}
