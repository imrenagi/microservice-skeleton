package com.imrenagi.service_auth.consumer;

import com.imrenagi.protobuf.GreetingGrpc;
import com.imrenagi.protobuf.HelloRequest;
import com.imrenagi.service_auth.service.UserServiceImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Component;

/**
 * Created by inagi on 6/6/17.
 */
@EnableEurekaClient
@Component
public class GreeterServiceConsumer {

//    @Autowired
//    private EurekaClient client;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final Logger logger = LoggerFactory.getLogger(GreeterServiceConsumer.class);

    public void greet(String name, String message) {

        ServiceInstance server = discoveryClient.getInstances("service-account").get(0);

        String hostName = server.getHost();
        int gRpcPort = Integer.parseInt(server.getMetadata().get("grpc.port"));
        logger.info("=====>> " + hostName + " ---- " + gRpcPort);

        final ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, gRpcPort)
                .usePlaintext(true)
                .build();
        final GreetingGrpc.GreetingFutureStub stub = GreetingGrpc.newFutureStub(channel);

        stub.sayHi(HelloRequest.newBuilder().setName(name).setMessage(message).build());
    }


}
