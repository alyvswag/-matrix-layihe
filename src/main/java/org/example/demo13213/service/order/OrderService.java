package org.example.demo13213.service.order;

import org.example.demo13213.model.dao.OrderItems;
import org.example.demo13213.model.dao.Orders;

import java.util.List;

public interface OrderService {
    Orders checkoutOrder();

    void confirmPayment(Long id);

    void cancelPayment(Long id);

    List<OrderItems> myOrders();
}