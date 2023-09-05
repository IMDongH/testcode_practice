package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor// 역직렬화 시 ObjectMapper 가 기본생성자를 사용한다.
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;
    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingType sellingType;
    @NotBlank(message = "상품 이름은 필수입니다.") //공백이어도안되고 ""여도 안된다.
//    @NotNull - "" " " 이런 문자는 통과가 된다.
//    @NotEmpty - 공백인 경우 통과
    private String name;
    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;

    @Builder

    public ProductCreateRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .name(name)
                .sellingType(sellingType)
                .price(price)
                .type(type).build();
    }

    public ProductCreateServiceRequest toServiceRequest(){
        return ProductCreateServiceRequest.builder()
                .name(name)
                .sellingType(sellingType)
                .price(price)
                .type(type).build();
    }
}
