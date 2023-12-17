package tw.iancheng.giftcardsystem.service;

import org.springframework.data.domain.Page;
import tw.iancheng.giftcardsystem.dto.order.OrderCreateRequest;
import tw.iancheng.giftcardsystem.dto.order.OrderQueryParams;
import tw.iancheng.giftcardsystem.model.Order;

public interface OrderService {
    Page<Order> getOrders(OrderQueryParams orderQueryParams);

    Order createOrder(OrderCreateRequest orderCreateRequest, Long userId);
}
