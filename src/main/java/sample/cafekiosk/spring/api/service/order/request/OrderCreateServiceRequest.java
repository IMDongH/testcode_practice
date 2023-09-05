package sample.cafekiosk.spring.api.service.order.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    @NotEmpty(message = "상품 번호는 1개 이상 존재해야 합니다.")
    private List<String> productNumber;

    @Builder
    private OrderCreateServiceRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }
}
