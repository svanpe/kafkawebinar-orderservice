package be.tobania.demo.kafka.orderService.service.mapper;


import be.tobania.demo.kafka.orderService.entities.OrderEntity;
import be.tobania.demo.kafka.orderService.model.Customer;
import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.OrderItem;
import be.tobania.demo.kafka.orderService.model.Product;
import be.tobania.demo.kafka.orderService.model.enums.StatusEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityApiMapper {

    public static Order mapOrder(OrderEntity order){

       Customer customer = new Customer();
        customer.setId(order.getCustomerEntity().getId());
        customer.setEmail(order.getCustomerEntity().getEmail());
        customer.setFirstName(order.getCustomerEntity().getFirstName());
        customer.setLastName(order.getCustomerEntity().getLastName());

       List<OrderItem> orderItemList = order.getOrderItems().stream().map(item->{
            OrderItem orderItem = new OrderItem();

            orderItem.setId(item.getId());
            orderItem.setQuantity(item.getQuantity());

           Product product = new Product();

           product.setId(item.getProductEntity().getId());
           product.setDescription(item.getProductEntity().getName());
           product.setName(item.getProductEntity().getDescription());

           orderItem.setProduct(product);
            return orderItem;
        }).collect(Collectors.toList());


        Order orderApi = new Order();
        orderApi.setId(order.getId());
        orderApi.setCreationDate(order.getCreationDate());
        orderApi.setCustomer(customer);
        orderApi.setStatus(StatusEnum.fromValue(order.getStatus()));
        orderApi.setOrderItems(orderItemList);

        return orderApi;

    }

}
