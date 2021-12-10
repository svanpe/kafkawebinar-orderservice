package be.tobania.demo.kafka.orderService.service;

import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.OrderForPatch;
import be.tobania.demo.kafka.orderService.model.Parcel;
import be.tobania.demo.kafka.orderService.model.Payment;
import be.tobania.demo.kafka.orderService.model.enums.OrderStatus;
import be.tobania.demo.kafka.orderService.model.enums.ParcelStatus;
import be.tobania.demo.kafka.orderService.model.enums.PaymentStatus;
import be.tobania.demo.kafka.orderService.repository.OrderRepository;
import be.tobania.demo.kafka.orderService.service.mapper.OrderApiEntityMapper;
import be.tobania.demo.kafka.orderService.service.mapper.OrderEntityApiMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDER_TOPIC = "orders";
    private static final String PAYMENT_TOPIC = "payments";
    private static final String SHIPPING_TOPIC = "shipping";


    public final OrderRepository orderRepository;

    private final KafkaTemplate<String, Order> kafkaTemplate;

    private final ObjectMapper objectMapper;


    @Transactional
    public Order createOrder(Order order) {

        final OrderEntity orderEntities = OrderApiEntityMapper.mapOrder(order);

        OrderEntity saveOrder = orderRepository.saveAndFlush(orderEntities);

        Order addedOrder = OrderEntityApiMapper.mapOrder(saveOrder);

        publishOrder(addedOrder);

        return addedOrder;
    }

    @Transactional
    public List<Order> getOrdersByStatus(OrderStatus status) {

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

        Order orderPatched = OrderEntityApiMapper.mapOrder(patchOrder);

        publishOrder(orderPatched);

        return orderPatched;
    }


    @Transactional
    public Order getOrdersById(Long orderId) {

        OrderEntity orderEntities = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No order for the given status"));
        return OrderEntityApiMapper.mapOrder(orderEntities);
    }


    public List<Order> getOrderByCustomer(Long customerRef) {

        List<OrderEntity> orderEntities = orderRepository.findOrderEntitiesByCustomerEntity_Id(customerRef);

        if (orderEntities == null || orderEntities.isEmpty()) {
            throw new RuntimeException("No order for the given status");
        }
        return orderEntities.stream()
                .map(OrderEntityApiMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Async
    public void publishOrder(Order order) {

        log.info("start publishing order");

        ListenableFuture<SendResult<String, Order>> future = kafkaTemplate.send(ORDER_TOPIC, order.getId().toString(), order);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Order>>() {
            @Override
            public void onSuccess(SendResult<String, Order> result) {
                log.info(String.format("Produced event to topic %s: key = %-10s", ORDER_TOPIC, order.getId().toString()));
            }
            @Override
            public void onFailure(Throwable ex) {
                ex.printStackTrace();
            }
        });

        log.info("order published with status %s", order.getStatus().name());

    }


    @KafkaListener(topics = PAYMENT_TOPIC, groupId = "order-service-consumer-id")
    public void consumePayment(String message) throws IOException {

        Payment payment = objectMapper.readValue(message, Payment.class);

        log.info(String.format("Consumed new payment with status-> %s", payment.getStatus().name()));

        if (payment.getStatus() == PaymentStatus.PAYED) {
            Order order = payment.getOrder();
            OrderForPatch orderForPatch = new OrderForPatch();
            orderForPatch.setStatus(OrderStatus.PAYED);

            patchOrder(orderForPatch, order.getId());
        }
    }

    @KafkaListener(topics = SHIPPING_TOPIC, groupId = "order-service-consumer-id")
    public void consumeParcel(String message) throws IOException {

        Parcel parcel = objectMapper.readValue(message, Parcel.class);

        log.info(String.format("Consumed new order with status-> %s", parcel.getStatus().name()));

        if (parcel.getStatus() == ParcelStatus.DELIVERED || parcel.getStatus() == ParcelStatus.IN_DELIVERY) {

            Order order = parcel.getOrder();
            OrderForPatch orderForPatch = new OrderForPatch();
            orderForPatch.setStatus(parcel.getStatus() == ParcelStatus.DELIVERED ? OrderStatus.DELIVERED : OrderStatus.SHIPPED);
            patchOrder(orderForPatch, order.getId());

        }
    }

}
