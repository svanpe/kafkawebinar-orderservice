package be.tobania.demo.kafka.orderService.service;

import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.OrderForPatch;
import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import be.tobania.demo.kafka.orderService.repository.OrderRepository;
import be.tobania.demo.kafka.orderService.service.mapper.OrderApiEntityMapper;
import be.tobania.demo.kafka.orderService.service.mapper.OrderEntityApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    public final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Order order) {

        final OrderEntity orderEntities = OrderApiEntityMapper.mapOrder(order);

        OrderEntity saveOrder = orderRepository.saveAndFlush(orderEntities);

        return OrderEntityApiMapper.mapOrder(saveOrder);
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

        return OrderEntityApiMapper.mapOrder(patchOrder);
    }


    @Transactional
    public Order getOrdersById(Long orderId) {

        OrderEntity orderEntities = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No order for the given status"));
        return OrderEntityApiMapper.mapOrder(orderEntities);
    }

}
