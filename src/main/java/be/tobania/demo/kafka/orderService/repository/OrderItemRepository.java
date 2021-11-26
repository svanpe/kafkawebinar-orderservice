package be.tobania.demo.kafka.orderService.repository;

import be.tobania.demo.kafka.orderService.entities.OrderItemEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItemEntity, Long> {
}
