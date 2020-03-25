package com.register.move.service.mp;

import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.client.RegistryNamingClientFactory;
import com.register.move.service.core.config.RegisterClientConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadFactory;

public class RmsMasterProcessApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        final  String driver = "com.register.move.service.plugin.nacos.client.NacosNamingClient";
        final  String driver0 = "com.register.move.service.plugin.eureka.client.EurekaNamingService";
        RegisterClientConfig registerConfig = new RegisterClientConfig();
        registerConfig.setServerAddr("http://127.0.0.1:7003/eureka/");
        registerConfig.setClassName(driver0);
        RegisterClient server = RegistryNamingClientFactory.createServer(registerConfig);
        System.out.println(server);
        ThreadFactory factory = new DefaultManagedAwareThreadFactory();
    }

}
