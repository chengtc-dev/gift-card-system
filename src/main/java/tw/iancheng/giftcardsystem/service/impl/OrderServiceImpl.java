package tw.iancheng.giftcardsystem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import tw.iancheng.giftcardsystem.dto.order.OrderQueryParams;
import tw.iancheng.giftcardsystem.model.Order;
import tw.iancheng.giftcardsystem.repository.OrderRepository;
import tw.iancheng.giftcardsystem.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> getOrders(OrderQueryParams orderQueryParams) {
        return orderRepository.findAllByUserId(orderQueryParams.getUserId(), orderQueryParams.getPageRequest());
    }
}
