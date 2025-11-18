package org.example.demo13213.controller.order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.demo13213.model.dao.Orders;
import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dto.response.base.BaseResponse;
import org.example.demo13213.service.order.OrderService;
import org.hibernate.query.Order;
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

}
