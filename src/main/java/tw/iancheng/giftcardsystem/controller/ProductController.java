package tw.iancheng.giftcardsystem.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tw.iancheng.giftcardsystem.dto.ProductQueryParams;
import tw.iancheng.giftcardsystem.dto.ProductRequest;
import tw.iancheng.giftcardsystem.model.Product;
import tw.iancheng.giftcardsystem.service.ProductService;

@Validated
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            // 查詢條件 Filtering
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,

            // 排序 Sorting
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") Boolean desc,

            // 分頁 Pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer pageSize,
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber
    ) {
        ProductQueryParams productQueryParams = ProductQueryParams.builder()
                .categoryId(categoryId).search(search)
                .pageRequest(setPageRequest(pageNumber, pageSize, desc, sort))
                .build();

        Page<Product> products = productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Validated ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody @Validated ProductRequest productRequest,
                                                 @PathVariable Long id) {
        Product product = productService.updateProduct(productRequest, id);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    private PageRequest setPageRequest(Integer pageNumber, Integer pageSize, Boolean desc, String sort) {
        Sort.Direction direction = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sort));
    }
}
