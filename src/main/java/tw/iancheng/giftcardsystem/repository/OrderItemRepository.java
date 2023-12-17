package tw.iancheng.giftcardsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.iancheng.giftcardsystem.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
