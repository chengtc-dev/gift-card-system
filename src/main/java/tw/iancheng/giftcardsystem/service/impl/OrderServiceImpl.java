package tw.iancheng.giftcardsystem.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tw.iancheng.giftcardsystem.dto.order.BuyItem;
import tw.iancheng.giftcardsystem.dto.order.OrderCreateRequest;
import tw.iancheng.giftcardsystem.dto.order.OrderQueryParams;
import tw.iancheng.giftcardsystem.model.Order;
import tw.iancheng.giftcardsystem.model.OrderItem;
import tw.iancheng.giftcardsystem.model.Product;
import tw.iancheng.giftcardsystem.repository.OrderItemRepository;
import tw.iancheng.giftcardsystem.repository.OrderRepository;
import tw.iancheng.giftcardsystem.repository.ProductRepository;
import tw.iancheng.giftcardsystem.repository.UserRepository;
import tw.iancheng.giftcardsystem.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<Order> getOrders(OrderQueryParams orderQueryParams) {
        return orderRepository.findAllByUserId(orderQueryParams.getUserId(), orderQueryParams.getPageRequest());
    }

    @Override
    public Order createOrder(OrderCreateRequest orderCreateRequest, Long userId) {
        if (!userRepository.existsById(userId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Order order = generateOrderId();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = new BigDecimal(0);

        for (BuyItem buyItem : orderCreateRequest.getBuyItems()) {
            Product product = productRepository.findById(buyItem.getProductId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST));

            if (!validateProductStockQuantity(product, buyItem))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            updateProductStockQuantity(product, buyItem);

            BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(buyItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .amount(amount).product(product).quantity(buyItem.getQuantity())
                    .build();

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(amount);
        }

        orderItemRepository.saveAll(orderItems);

        order.setOrderItems(orderItems);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    private Order generateOrderId() {
        Order order = Order.builder().build();

        return orderRepository.saveAndFlush(order);
    }

    private boolean validateProductStockQuantity(Product product, BuyItem buyItem) {
        return product.getStockQuantity() >= buyItem.getQuantity();
    }

    private void updateProductStockQuantity(Product product, BuyItem buyItem) {
        product.setStockQuantity(product.getStockQuantity() - buyItem.getQuantity());

        productRepository.save(product);
    }

}
