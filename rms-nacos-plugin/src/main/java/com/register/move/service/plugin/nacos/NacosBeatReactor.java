package com.register.move.service.plugin.nacos;

import com.alibaba.nacos.client.naming.NacosNamingService;
import com.register.move.service.core.beat.AbstractHeartBeat;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import com.register.move.service.plugin.nacos.domain.NacosInstanceConverter;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class NacosBeatReactor extends AbstractHeartBeat<NacosInstanceConverter> {

    final Map<String, Converter<NacosInstanceConverter>> beat = new ConcurrentHashMap<>();

    public NacosBeatReactor(Map<String, Converter<NacosInstanceConverter>> beat, String threadName) {
        super(beat, threadName);
        this.beat.putAll(beat);
    }

    @Override
    protected Runnable beatThread() {
        return () -> {
            AtomicReference<Properties> atomicReference = new AtomicReference<>(new Properties());
            beat.values().forEach(converter -> {
                StandardServiceMetadata metadata = converter.converter();
                atomicReference.set(new Properties());
                Properties properties = atomicReference.get();
                properties.put("serverAddr", metadata.getIp() + ":" + metadata.getPort());
                NacosNamingService service = new NacosNamingService(properties);
            });
        };
    }
}