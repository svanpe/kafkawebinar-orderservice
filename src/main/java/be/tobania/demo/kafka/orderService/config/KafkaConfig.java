package be.tobania.demo.kafka.orderService.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic OrderToppic() {
        return TopicBuilder.name("orders")
                .partitions(2)
                .replicas(2)
                .compact()
                .build();
    }


}
