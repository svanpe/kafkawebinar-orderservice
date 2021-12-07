package be.tobania.demo.kafka.orderService.model;

import be.tobania.demo.kafka.shippingService.model.enums.ParcelStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Data
@Validated
@ApiModel("Contain shipping data")
@ToString
public class Parcel {
    private Long id;

    private ParcelStatus status;

    private Order order;

}
