package com.imrenagi.service_account.service;

import com.imrenagi.protobuf.GreetingGrpc;
import com.imrenagi.protobuf.HelloReply;
import com.imrenagi.protobuf.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

/**
 * Created by inagi on 6/6/17.
 */
@GRpcService
public class GreeterServiceImpl extends GreetingGrpc.GreetingImplBase {

    @Override
    public void sayHi(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        final HelloReply reply = HelloReply.newBuilder()
                .setName(request.getName())
                .setMessage("Hello " + request.getName())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
