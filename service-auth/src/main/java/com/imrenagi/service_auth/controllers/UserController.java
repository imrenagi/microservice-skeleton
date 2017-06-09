package com.imrenagi.service_auth.controllers;

import com.google.common.util.concurrent.ListenableFuture;
import com.imrenagi.protobuf.GreetingGrpc;
import com.imrenagi.protobuf.HelloReply;
import com.imrenagi.protobuf.HelloRequest;
import com.imrenagi.service_auth.consumer.GreeterServiceConsumer;
import com.imrenagi.service_auth.domain.User;
import com.imrenagi.service_auth.service.UserService;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by imrenagi on 5/9/17.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

//    @Autowired
//    GreeterServiceConsumer consumer = new GreeterServiceConsumer();

    @Autowired
    private UserService userService;

//    @Autowired
//    private EurekaClient eurekaClient;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        greet("imre", "this is message gan!");
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @RequestMapping(method = RequestMethod.POST)
    public void createUser(@Valid @RequestBody User user) {
        userService.create(user);
    }

//    private final Logger logger = LoggerFactory.getLogger(GreeterServiceConsumer.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    public void greet(String name, String message) {

//        if (eurekaClient == null) {
//            log.info("Eureka client is empty");
//        } else {
//            log.info("Eureka client is not empty");
//        }

        if (discoveryClient == null) {
            log.info("Discovery client is null");
        } else {
            log.info("Discovery client is not null");
            try {
                List<ServiceInstance> servers = discoveryClient.getInstances("service-account");

                for (ServiceInstance server : servers) {
                    String hostName = server.getHost();
                    log.info(String.format("Hostname => %s", hostName));

                    for (String key : server.getMetadata().keySet()) {
                        log.info(String.format("%s => %s : %s", hostName, key, server.getMetadata().get(key)));
                    }

                    int gRpcPort = 6565;//Integer.parseInt(server.getMetadata().get("grpc.port"));
                    log.info("=====>> " + hostName + " ---- " + gRpcPort);

                    final ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, gRpcPort)
                            .usePlaintext(true)
                            .build();
//                    final GreetingGrpc.GreetingFutureStub stub = GreetingGrpc.newFutureStub(channel);
                    final GreetingGrpc.GreetingStub stub = GreetingGrpc.newStub(channel);

                    log.info(String.format("Current time milis : %d", System.currentTimeMillis()));
                    stub.sayHi(HelloRequest.newBuilder().setName(name).setMessage(message).build(), new StreamObserver<HelloReply>() {
                        @Override
                        public void onNext(HelloReply r) {
                            log.info(String.format("Current time milis 2 : %d", System.currentTimeMillis()));
                            log.info(String.format("Replied : %s , %s", r.getName(), r.getMessage()));
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }

                        @Override
                        public void onCompleted() {

                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
