package com.register.move.service.domain;

import java.io.Serializable;

public interface Converter<T> extends Serializable {

    StandardServiceMetadata converter();

    T convertT(StandardServiceMetadata metadata);

    default Object get(){
       return null;
    }

    //boolean canConvert();
}
