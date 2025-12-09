package com.fdj.grpcclient.runner;

import com.fdj.grpcclient.service.UserClientService;
import com.fdj.grpcclient.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcClientRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GrpcClientRunner.class);

    private final UserClientService userClientService;

    public GrpcClientRunner(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting gRPC client demonstration...");
        
        try {
            // Example: Call AddUser with a test name
            String testUserName = args.length > 0 ? args[0] : "John Doe";
            UserResponse response = userClientService.addUser(testUserName);
            
            logger.info("=== gRPC Call Successful ===");
            logger.info("Response Message: {}", response.getMessage());
            logger.info("Response User: {}", response.getUser());
        } catch (Exception e) {
            logger.error("gRPC call failed: {}", e.getMessage(), e);
        }
    }
}

