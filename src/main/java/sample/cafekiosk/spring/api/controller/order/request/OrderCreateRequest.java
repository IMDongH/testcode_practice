package sample.cafekiosk.spring.api.controller.order.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호는 1개 이상 존재해야 합니다.")
    private List<String> productNumber;

    @Builder
    private OrderCreateRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }

    public OrderCreateServiceRequest toServiceRequest(){
        return OrderCreateServiceRequest.builder()
                .productNumber(productNumber).build();
    }
}
