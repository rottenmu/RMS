package com.register.move.service.mp.processor;

import com.register.move.service.mp.config.NacosConfig;
import com.register.move.service.processor.ServiceMetaDataOperationProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerServiceMetaDataOperationProcessor extends ServiceMetaDataOperationProcessor {

    public CustomerServiceMetaDataOperationProcessor(NacosConfig config) {
        super(config);
    }
}
