package be.tobania.demo.kafka.orderService.model;

import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Validated
@ApiModel("Contain order data")
public class Order {

    private Long id;

    private LocalDate creationDate;

    private Customer customer;

    private StatusEnum status;

    private List<OrderItem> orderItems;
}
