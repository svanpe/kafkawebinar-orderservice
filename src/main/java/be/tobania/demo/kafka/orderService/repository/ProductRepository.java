package be.tobania.demo.kafka.orderService.repository;

import be.tobania.demo.kafka.orderService.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
