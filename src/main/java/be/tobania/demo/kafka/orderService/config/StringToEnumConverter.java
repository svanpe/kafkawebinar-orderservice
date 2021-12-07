package be.tobania.demo.kafka.orderService.config;

import be.tobania.demo.kafka.orderService.model.enums.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(String input) {
        return OrderStatus.valueOf(input.toUpperCase());
    }
}