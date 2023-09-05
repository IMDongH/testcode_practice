package sample.cafekiosk.spring.api.service.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor// 역직렬화 시 ObjectMapper 가 기본생성자를 사용한다.
public class ProductCreateServiceRequest {

    private ProductType type;
    private ProductSellingType sellingType;
    private String name;
    private int price;

    @Builder

    public ProductCreateServiceRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
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
}
