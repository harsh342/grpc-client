package com.fdj.grpcclient.service;

import com.fdj.grpcclient.config.GrpcClientConfig;
import com.fdj.grpcclient.UserRequest;
import com.fdj.grpcclient.UserResponse;
import com.fdj.grpcclient.UserGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class UserClientService {
    private static final Logger logger = LoggerFactory.getLogger(UserClientService.class);

    private final GrpcClientConfig grpcClientConfig;
    private ManagedChannel channel;
    private UserGrpc.UserBlockingStub userStub;

    public UserClientService(GrpcClientConfig grpcClientConfig) {
        this.grpcClientConfig = grpcClientConfig;
        initializeChannel();
    }

    private void initializeChannel() {
        channel = ManagedChannelBuilder.forAddress(grpcClientConfig.getHost(), grpcClientConfig.getPort())
                .usePlaintext()
                .build();
        userStub = UserGrpc.newBlockingStub(channel);
        logger.info("gRPC channel initialized for server: {}", grpcClientConfig.getAddress());
    }

    public UserResponse addUser(String userName) {
        logger.info("Calling AddUser with name: {}", userName);
        
        UserRequest request = UserRequest.newBuilder()
                .setName(userName)
                .build();

        try {
            UserResponse response = userStub.addUser(request);
            logger.info("Received response - message: {}, user: {}", response.getMessage(), response.getUser());
            return response;
        } catch (Exception e) {
            logger.error("Error calling AddUser: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call AddUser: " + e.getMessage(), e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            logger.info("gRPC channel shutdown");
        }
    }
}

