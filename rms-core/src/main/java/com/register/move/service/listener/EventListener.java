package com.register.move.service.listener;



public interface EventListener<T>{

    void  onEvent(T event);
}
