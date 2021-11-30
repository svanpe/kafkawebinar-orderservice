package be.tobania.demo.kafka.orderService.model;

import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@Validated
@ApiModel("Contain order data")
@ToString
public class Order {

    private Long id;

    private LocalDate creationDate;

    private Customer customer;

    private StatusEnum status;

    private List<OrderItem> orderItems;
}
