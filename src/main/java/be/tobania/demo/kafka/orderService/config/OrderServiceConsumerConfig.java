package be.tobania.demo.kafka.orderService.config;

import be.tobania.demo.kafka.orderService.model.Parcel;
import be.tobania.demo.kafka.orderService.model.Payment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@PropertySource("classpath:application.properties")
@Configuration
public class OrderServiceConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.consumer.payment-service}")
    private String paymentGroupId;

    @Value("${spring.kafka.consumer.shipping-service}")
    private String shippingGroupId;


    @Bean
    public ConsumerFactory<String, Payment> paymentConsumerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, paymentGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new PaymentKafkaDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Payment> paymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Payment> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Parcel> parcelConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, shippingGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new ParcelKafkaDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Parcel> parcelKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Parcel> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(parcelConsumerFactory());
        return factory;
    }


}
