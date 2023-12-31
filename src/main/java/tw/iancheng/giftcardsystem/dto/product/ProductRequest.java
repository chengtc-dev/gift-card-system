package tw.iancheng.giftcardsystem.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductRequest {

    @NotNull
    private String name;
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private Long categoryId;

}