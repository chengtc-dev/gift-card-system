package tw.iancheng.giftcardsystem.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tw.iancheng.giftcardsystem.model.Product;

@Repository
public interface ProductRepository extends
        PagingAndSortingRepository<Product, Long>,
        QuerydslPredicateExecutor<Product> {
}
