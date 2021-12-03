package be.tobania.demo.kafka.orderService.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id ;
    private String firstName ;
    private String lastName ;
    private String email;

   @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL)
   private List<OrderEntity> orders;

}