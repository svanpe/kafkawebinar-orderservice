package be.tobania.demo.kafka.orderService.repository;

import be.tobania.demo.kafka.orderService.entities.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustormerRepository extends CrudRepository<CustomerEntity, Long> {
}
