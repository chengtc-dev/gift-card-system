package tw.iancheng.giftcardsystem.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.iancheng.giftcardsystem.dto.order.OrderQueryParams;
import tw.iancheng.giftcardsystem.model.Order;
import tw.iancheng.giftcardsystem.service.OrderService;

@Validated
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer pageSize,
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @PathVariable Long userId
    ) {
        OrderQueryParams orderQueryParams = OrderQueryParams.builder()
                .userId(userId).pageRequest(setPageRequest(pageNumber, pageSize))
                .build();

        Page<Order> orders = orderService.getOrders(orderQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    private PageRequest setPageRequest(Integer pageNumber, Integer pageSize) {
        return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
