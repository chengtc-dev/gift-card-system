package tw.iancheng.giftcardsystem.dto.order;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
@Builder
public class OrderQueryParams {

    private Long userId;
    private PageRequest pageRequest;

}
