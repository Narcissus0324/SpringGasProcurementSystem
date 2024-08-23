package com.mapInterfaceService;

import com.mapInterfaceService.service.OrToolsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(OrToolsProperties.class)
@SpringBootApplication
public class GasProcurementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(GasProcurementSystemApplication.class, args);
    }
}
