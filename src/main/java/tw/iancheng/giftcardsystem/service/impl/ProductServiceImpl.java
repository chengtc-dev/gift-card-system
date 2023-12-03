package tw.iancheng.giftcardsystem.service.impl;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import tw.iancheng.giftcardsystem.dto.ProductQueryParams;
import tw.iancheng.giftcardsystem.model.Product;
import tw.iancheng.giftcardsystem.model.QProduct;
import tw.iancheng.giftcardsystem.repository.ProductRepository;
import tw.iancheng.giftcardsystem.service.ProductService;

import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getProducts(ProductQueryParams productQueryParams) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QProduct.product.id.isNotNull());

        if (productQueryParams.getSearch() != null)
            booleanBuilder.and(QProduct.product.name.containsIgnoreCase(productQueryParams.getSearch()));

        if (productQueryParams.getCategoryId() != null)
            booleanBuilder.and(QProduct.product.category.id.eq(productQueryParams.getCategoryId()));

        return productRepository.findAll(Objects.requireNonNull(booleanBuilder.getValue()),
                productQueryParams.getPageRequest());
    }
}
