package be.tobania.demo.kafka.orderService.controller;

import be.tobania.demo.kafka.orderService.model.Order;
import be.tobania.demo.kafka.orderService.model.OrderForPatch;
import be.tobania.demo.kafka.orderService.model.enums.OrderStatus;
import be.tobania.demo.kafka.orderService.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;


    @ApiOperation(value = "Add a new order", nickname = "addOrder")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class),
            @ApiResponse(code = 405, message = "Invalid input")})
    @RequestMapping(produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public Order addOrder(@Valid @RequestBody @NotNull Order order) {
        log.info("add new order");
       return orderService.createOrder(order);
    }

    @ApiOperation(value = "Finds Orders by status", nickname = "findOrdersByStatus", notes = "Multiple status values can be provided with comma separated strings")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid status value")})
    @GetMapping(value = "/findByStatus",
            produces = {"application/json"})
    public List<Order> findOrdersByStatus(@NotNull @ApiParam(value = "Status values that need to be considered for filter", required = true) @Valid @RequestParam(value = "status", required = true) OrderStatus status) {
        log.info("get order by status");
        return orderService.getOrdersByStatus(status);
    }

    @ApiOperation(value = "Finds Orders by customer", nickname = "findOrderPerCustomer", notes = "give the customer reference.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid tag value")})
    @GetMapping(value = "/findByCustomer",
            produces = {"application/json"})
    public List<Order> findOrderPerCustomer(@RequestParam(name = "customerRef", required = true) Long customerRef) {
        return orderService.getOrderByCustomer(customerRef);
    }

    @ApiOperation(value = "Update the status of an existing order", nickname = "updateOrderStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Order not found"),
            @ApiResponse(code = 405, message = "Validation exception")})
    @PatchMapping(value = "/{orderId}",
            produces = {"application/json"},
            consumes = {"application/json"})
    public Order updateOrderStatus(@ApiParam(value = "Order object that needs to be updated", required = true) @Valid @RequestBody OrderForPatch orderForPatch, @ApiParam(value = "ID of order to return", required = true) @PathVariable("orderId") Long orderId) {
        log.info("patch order");
        return orderService.patchOrder(orderForPatch, orderId);
    }

    @ApiOperation(value = "Find order by ID", nickname = "getOrderById", notes = "Returns a single order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Order.class),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "Pet not found")})
    @GetMapping(value = "/{orderId}",
            produces = {"application/json"})
    public Order getOrderById(@ApiParam(value = "ID of order to return", required = true) @PathVariable("orderId") Long orderId) {
        return orderService.getOrdersById(orderId);
    }


}
