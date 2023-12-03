package tw.iancheng.giftcardsystem.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

@Getter
@Builder
public class ProductQueryParams {

    private Long categoryId;
    private String search;
    private String orderBy;
    private PageRequest pageRequest;

}
