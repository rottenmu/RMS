package com.register.move.service.event;

import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.domain.StandardServiceMetadata;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class DaoEvent {
    private String dataId;
    private String group;
    private List<StandardServiceMetadata> metaData;
    private  long timeoutMs;
    private String destConvertClassName;
    private RegisterType destRegisterType;

}

