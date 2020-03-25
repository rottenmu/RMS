package com.register.move.service.mp.spring.listener;

import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.client.RegistryNamingClientFactory;
import com.register.move.service.core.config.RegisterClientConfig;
import com.register.move.service.mp.bus.CustomerEventBus;
import com.register.move.service.mp.callbak.StopAppEventListener;
import com.register.move.service.mp.config.NacosConfig;
import com.register.move.service.mp.config.RegisterMoveConfig;
import com.register.move.service.mp.event.AppEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import static com.register.move.service.core.client.RegistryNamingClientFactory.*;

@Component
@Order(5)
public class RegistryClientEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private  final  static  ThreadLocal<RegisterClient> DESTCLI = new ThreadLocal<>();
    private  final  static  ThreadLocal<RegisterClient> SOURCECLI = new ThreadLocal<>();

    @Override
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {

        if(Objects.nonNull(event)){
            ApplicationContext applicationContext = event.getApplicationContext();
            NacosConfig nacosConfig = applicationContext.getBean(NacosConfig.class);
            RegisterMoveConfig moveConfig = applicationContext.getBean(RegisterMoveConfig.class);
            //init config
            RegisterClientConfig clientConfig = new RegisterClientConfig();
            clientConfig.setServerAddr(moveConfig.getDestinationServerAddr());
            clientConfig.setServerAddr(moveConfig.getDestinationServerAddr());
            RegisterType destType = RegisterType.typeMap.get(moveConfig.getDestinationRegisterType().toLowerCase());
            clientConfig.setRegisterType(destType);
            clientConfig.setClassName(moveConfig.getDestDriverClass());
            RegisterClient destClient = null;
            try {
                destClient = createServer(clientConfig);
                DESTCLI.set(destClient);
                RegisterCache.getCache().put(destType,destClient);
            } catch (Exception e) {
                e.printStackTrace();
            }

            clientConfig.setServerAddr(null);
            clientConfig.setRegisterType(null);
            clientConfig =new RegisterClientConfig();
            clientConfig.setServerAddr(moveConfig.getSourceServerAddr());
            RegisterType sourceType = RegisterType.typeMap.get(moveConfig.getSourceRegisterType().toLowerCase());
            clientConfig.setRegisterType(sourceType);
            clientConfig.setClassName(moveConfig.getSourceDriverClass());
            RegisterClientConfig finalClientConfig = clientConfig;
            FutureTask<RegisterClient>  task = new FutureTask<RegisterClient>(() ->{
                try {
                    return createServer(finalClientConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                    CustomerEventBus.getEventBus(new StopAppEventListener()).post(new AppEvent());
                }
                return  null;
            });
            Thread thread = new Thread(task);
            thread.start();
            clientConfig.setServerAddr(moveConfig.getSourceServerAddr());
            RegisterClient sourceClient = null;
            try {
                sourceClient = task.get();
                SOURCECLI.set(sourceClient);
                RegisterCache.getCache().set(sourceType,sourceClient);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                CustomerEventBus.getEventBus(new StopAppEventListener()).post(new AppEvent());
            }
        }
    }
}
