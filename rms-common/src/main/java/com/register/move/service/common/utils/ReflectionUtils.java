package com.register.move.service.common.utils;

public class ReflectionUtils {

    public  static Object createInstance(String className){
        Object object = null;
        try {
            Class<?> aClass = Class.forName(className);
            try {
                object = aClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
