package com.register.move.service.plugin.nacos.event;

import com.register.move.service.event.BaseEvent;
import com.register.move.service.plugin.nacos.domain.NacosInstanceConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Data
public class NacosEvent extends BaseEvent {

    private CopyOnWriteArrayList<NacosInstanceConverter> instanceInfos = new CopyOnWriteArrayList<>();
}
