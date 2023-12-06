package tw.iancheng.giftcardsystem.service;

import org.springframework.data.domain.Page;
import tw.iancheng.giftcardsystem.dto.product.ProductQueryParams;
import tw.iancheng.giftcardsystem.dto.product.ProductRequest;
import tw.iancheng.giftcardsystem.model.Product;

public interface ProductService {
    Page<Product> getProducts(ProductQueryParams productQueryParams);

    Product createProduct(ProductRequest productRequest);

    Product updateProduct(ProductRequest productRequest, Long id);

    void deleteProduct(Long id);
}
