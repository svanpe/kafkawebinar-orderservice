package be.tobania.demo.kafka.orderService.config;

import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, StatusEnum> {
    @Override
    public StatusEnum convert(String input) {
        return StatusEnum.valueOf(input.toUpperCase());
    }
}