package tw.iancheng.giftcardsystem.service;

import org.springframework.data.domain.Page;
import tw.iancheng.giftcardsystem.dto.ProductQueryParams;
import tw.iancheng.giftcardsystem.dto.ProductRequest;
import tw.iancheng.giftcardsystem.model.Product;

public interface ProductService {
    Page<Product> getProducts(ProductQueryParams productQueryParams);

    Product createProduct(ProductRequest productRequest);
}
