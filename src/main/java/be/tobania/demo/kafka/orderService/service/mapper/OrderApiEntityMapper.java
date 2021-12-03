package be.tobania.demo.kafka.orderService.service.mapper;

import be.tobania.demo.kafka.orderService.entities.CustomerEntity;
import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.entities.OrderItemEntity;
import be.tobania.demo.kafka.orderService.entities.ProductEntity;
import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Transactional
public class OrderApiEntityMapper {


    public static OrderEntity mapOrder(Order order){

        CustomerEntity customer = new CustomerEntity();
                customer.setEmail(order.getCustomer().getEmail());
                customer.setFirstName(order.getCustomer().getFirstName());
                customer.setLastName(order.getCustomer().getLastName());

        List<OrderItemEntity> orderItemList = order.getOrderItems().stream().map(item->{
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setQuantity(item.getQuantity());

            ProductEntity productEntity = new ProductEntity();
            productEntity.setName(item.getProduct().getName());
            productEntity.setDescription(item.getProduct().getDescription());

            orderItem.setProductEntity(productEntity);
            return orderItem;
        }).collect(Collectors.toList());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreationDate(order.getCreationDate());
        orderEntity.setCustomerEntity(customer);
        orderEntity.setStatus(order.getStatus().getValue());
        orderEntity.setOrderItems(orderItemList);

        return orderEntity;

    }



}
