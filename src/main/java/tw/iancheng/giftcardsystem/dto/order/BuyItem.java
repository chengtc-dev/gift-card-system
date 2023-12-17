package tw.iancheng.giftcardsystem.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BuyItem {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

}
