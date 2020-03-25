package com.register.move.service.plugin.consul.client;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.query.model.QueryExecution;
import com.ecwid.consul.v1.query.model.QueryNode;
import com.register.move.service.common.cache.RegisterCache;
import com.register.move.service.common.constants.RegisterType;
import com.register.move.service.core.RegisterClient;
import com.register.move.service.core.config.RegisterClientConfig;

import java.util.List;
import java.util.Objects;

public class ConsulNamingClient extends ConsulClient implements RegisterClient<NewService, ConsulNamingClient> {


    private  ConsulClient consulClient;

    public  ConsulNamingClient(String agentHost, int agentPort){
        super(agentHost,agentPort);
    }
    public  ConsulNamingClient(){}
    public  ConsulNamingClient(RegisterClientConfig clientConfig){

        try {
            this.consulClient = this.init(clientConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ConsulNamingClient init(RegisterClientConfig registerConfig) throws Exception {

        ConsulNamingClient client = new ConsulNamingClient(registerConfig.getAddress(),registerConfig.getPort());
        if (Objects.nonNull(REGISTERMAP.get(RegisterType.CONSUL))){
            return ((ConsulNamingClient) REGISTERMAP.get(getRegisterType()));
        }
        RegisterCache.getCache().put(RegisterType.NACOS,client);

        return client;
    }

    @Override
    public void registerInstance(NewService newService) {

        Response<Void> response = this.consulClient.agentServiceRegister(newService);
    }

    @Override
    public void deregisterInstance(NewService newService) {

        this.consulClient.agentServiceDeregister(newService.getId());

    }

    @Override
    public List<NewService> serviceInstanceInfo(String serviceName, String region) throws Exception {

//        QueryParams params = new QueryParams();
//        Response<QueryExecution> queryExecutionResponse = this.consulClient.executePreparedQuery(params);
//        List<QueryNode> nodes = queryExecutionResponse.getValue().getNodes();
        return null;
    }

    @Override
    public RegisterType getRegisterType() {
        return RegisterType.CONSUL;
    }

    @Override
    public RegisterClient<NewService, ConsulNamingClient> setRegister(ConsulNamingClient consulClient) {
        return null;
    }
}
