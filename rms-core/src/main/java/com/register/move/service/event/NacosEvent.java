package com.register.move.service.event;

import com.register.move.service.domain.InstanceUseForNacos;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Data
public class NacosEvent extends  BaseEvent {

    private CopyOnWriteArrayList<InstanceUseForNacos> instanceInfos = new CopyOnWriteArrayList<>();
}
