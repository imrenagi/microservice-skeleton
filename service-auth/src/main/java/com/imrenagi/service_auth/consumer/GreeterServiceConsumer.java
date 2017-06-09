package com.imrenagi.service_auth.consumer;

import com.imrenagi.protobuf.GreetingGrpc;
import com.imrenagi.protobuf.HelloRequest;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by inagi on 6/6/17.
 */
//@Component
public class GreeterServiceConsumer {

    private final Logger logger = LoggerFactory.getLogger(GreeterServiceConsumer.class);

    @Autowired
    private DiscoveryClient discoveryClient;
//    @Autowired
//    private EurekaClient client;

    public void greet(String name, String message) {

        if (discoveryClient == null) {
            logger.info("Discovery client is null");
        } else {
            logger.info("Discovery client is not null");
            try {
                List<ServiceInstance> servers = discoveryClient.getInstances("service-account");

                for (ServiceInstance server : servers) {
                    String hostName = server.getHost();
                    int gRpcPort = Integer.parseInt(server.getMetadata().get("grpc.port"));
                    logger.info("=====>> " + hostName + " ---- " + gRpcPort);

                    final ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, gRpcPort)
                            .usePlaintext(true)
                            .build();
                    final GreetingGrpc.GreetingFutureStub stub = GreetingGrpc.newFutureStub(channel);

                    stub.sayHi(HelloRequest.newBuilder().setName(name).setMessage(message).build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
