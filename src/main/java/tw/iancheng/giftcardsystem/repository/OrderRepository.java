package tw.iancheng.giftcardsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import tw.iancheng.giftcardsystem.model.Order;

public interface OrderRepository extends
        PagingAndSortingRepository<Order, Long>,
        JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
}