package be.tobania.demo.kafka.orderService.config;

import be.tobania.demo.kafka.orderService.model.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class PaymentKafkaDeserializer implements Deserializer<Payment> {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Payment deserialize(String s, byte[] data) {
        try {
            if (data == null) {
                log.info("Data to serialized is null");
                return null;
            }
            log.info("Serializing...");
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(new String(data), Payment.class);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing order to byte[]");
        }
    }


    @Override
    public void close() {
    }


}
