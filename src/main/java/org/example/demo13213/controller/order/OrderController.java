package org.example.demo13213.controller.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.model.dao.Orders;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.service.order.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderController {

    final OrderService orderService;

    @PostMapping("/checkout")
    public BaseResponse<Orders> checkout() {
        return BaseResponse.success(orderService.checkoutOrder());
    }

    @PostMapping("/{id}/confirm-payment")
    public BaseResponse<Void> confirmPayment(@PathVariable("id") Long id) {
        orderService.confirmPayment(id);
        return BaseResponse.success();
    }

    @PostMapping("/{id}/cancel-payment")
    public BaseResponse<Void> cancelPayment(@PathVariable("id") Long id) {
        orderService.cancelPayment(id);
        return BaseResponse.success();
    }

    @PostMapping("/my")
    public BaseResponse<List<OrderItems>> myOrders() {
        return BaseResponse.success(orderService.myOrders());
    }
}