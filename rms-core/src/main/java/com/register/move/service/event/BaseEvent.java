package com.register.move.service.event;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.domain.Converter;
import com.register.move.service.domain.StandardServiceMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class BaseEvent {

    private List<StandardServiceMetadata> metaData;
    private String destConvertClassName;
    private RegisterType destRegisterType;
    private Runnable runnable;
}
