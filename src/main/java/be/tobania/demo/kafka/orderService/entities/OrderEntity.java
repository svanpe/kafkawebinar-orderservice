package be.tobania.demo.kafka.orderService.entities;

import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CustomerEntity customerEntity;

    private String status;

    @OneToMany(mappedBy ="orderEntity" ,cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;

}