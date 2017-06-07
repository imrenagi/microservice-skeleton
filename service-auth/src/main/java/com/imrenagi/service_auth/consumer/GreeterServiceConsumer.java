package com.imrenagi.service_auth.consumer;

import com.imrenagi.protobuf.GreetingGrpc;
import com.imrenagi.protobuf.HelloRequest;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Component;

/**
 * Created by inagi on 6/6/17.
 */
@EnableEurekaClient
@Component
public class GreeterServiceConsumer {

    @Autowired
    private EurekaClient client;

    public void greet(String name, String message) {
        InstanceInfo instanceInfo = client.getNextServerFromEureka("service-account", false);
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext(true)
                .build();
        final GreetingGrpc.GreetingFutureStub stub = GreetingGrpc.newFutureStub(channel);
        stub.sayHi(HelloRequest.newBuilder().setName(name).setMessage(message).build());
    }


}
