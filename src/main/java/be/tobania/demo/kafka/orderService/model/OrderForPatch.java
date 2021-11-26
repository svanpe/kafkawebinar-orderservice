package be.tobania.demo.kafka.orderService.model;

import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;


@Validated
@Getter
@Setter
@NoArgsConstructor
public class OrderForPatch {

    private StatusEnum status;

}
