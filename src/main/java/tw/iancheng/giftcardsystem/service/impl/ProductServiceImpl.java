package tw.iancheng.giftcardsystem.service.impl;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tw.iancheng.giftcardsystem.dto.ProductQueryParams;
import tw.iancheng.giftcardsystem.dto.ProductRequest;
import tw.iancheng.giftcardsystem.model.Category;
import tw.iancheng.giftcardsystem.model.Product;
import tw.iancheng.giftcardsystem.model.QProduct;
import tw.iancheng.giftcardsystem.repository.CategoryRepository;
import tw.iancheng.giftcardsystem.repository.ProductRepository;
import tw.iancheng.giftcardsystem.service.ProductService;

import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Product product = buildProduct(productRequest);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductRequest productRequest, Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        updateProduct(productRequest, product);

        return productRepository.save(product);
    }

    private Product buildProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST));

        return Product.builder()
                .name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity()).category(category)
                .build();
    }

    private void updateProduct(ProductRequest productRequest, Product product) {
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(category);
    }

}
