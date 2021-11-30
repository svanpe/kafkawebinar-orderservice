package be.tobania.demo.kafka.orderService.service;

import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.OrderForPatch;
import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import be.tobania.demo.kafka.orderService.repository.OrderRepository;
import be.tobania.demo.kafka.orderService.service.mapper.OrderApiEntityMapper;
import be.tobania.demo.kafka.orderService.service.mapper.OrderEntityApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDER_TOPIC = "orders";

    public final OrderRepository orderRepository;

    private final KafkaTemplate<String, Order> kafkaTemplate;



    @Transactional
    public Order createOrder(Order order) {

        final OrderEntity orderEntities = OrderApiEntityMapper.mapOrder(order);

        OrderEntity saveOrder = orderRepository.saveAndFlush(orderEntities);

        Order  addedOrder =  OrderEntityApiMapper.mapOrder(saveOrder);

        publishOrder(addedOrder);

        return addedOrder;
    }

    @Transactional
    public List<Order> getOrdersByStatus(StatusEnum status) {

        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByStatus(status.getValue());
        if (orderEntities == null || orderEntities.isEmpty()) {
            throw new RuntimeException("No order for the given status");
        }
        return orderEntities.stream()
                .map(OrderEntityApiMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Transactional
    public Order patchOrder(OrderForPatch orderForPatch, Long orderId) {

        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("No order for this id"));

        orderEntity.setStatus(orderForPatch.getStatus().getValue());

        OrderEntity patchOrder = orderRepository.save(orderEntity);
        log.info("Order patched ...");
        Order orderPatched =  OrderEntityApiMapper.mapOrder(patchOrder);

        publishOrder(orderPatched);

        return orderPatched;
    }


    @Transactional
    public Order getOrdersById(Long orderId) {

        OrderEntity orderEntities = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No order for the given status"));
        return OrderEntityApiMapper.mapOrder(orderEntities);
    }


    public List<Order> getOrderByCustomer(Long customerRef){

        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByCustomerEntity_Id(customerRef);

        if (orderEntities == null || orderEntities.isEmpty()) {
            throw new RuntimeException("No order for the given status");
        }
        return orderEntities.stream()
                .map(OrderEntityApiMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Async
    public void publishOrder(Order order){

        log.info("start publishing order");

        kafkaTemplate.send(ORDER_TOPIC, order.getId().toString(), order);

        log.info("order published");

    }

}
