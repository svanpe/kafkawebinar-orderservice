package be.tobania.demo.kafka.orderService.repository;

import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    public List<OrderEntity> findOrderByStatus(StatusEnum statusEnum);
}
