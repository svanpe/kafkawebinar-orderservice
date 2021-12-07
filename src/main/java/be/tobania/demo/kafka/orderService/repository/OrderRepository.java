package be.tobania.demo.kafka.orderService.repository;

import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    public List<OrderEntity> findOrderEntitiesByStatus(String statusEnum);

    public List<OrderEntity> findOrderEntitiesByCustomerEntity_Id(Long id);
}
