package be.tobania.demo.kafka.orderService.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi OrderApi() {
        return GroupedOpenApi.builder()
                .group("be.tobania.demo.kafka.orderService.model")
                .packagesToScan("be.tobania.demo.kafka.orderService.controller")
                .pathsToMatch("/")
                .build();
    }


}