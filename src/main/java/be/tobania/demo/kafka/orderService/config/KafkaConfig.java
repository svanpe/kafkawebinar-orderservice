package be.tobania.demo.kafka.orderService.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:application.properties")
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic OrderToppic() {
        return TopicBuilder.name("orders")
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }


}
