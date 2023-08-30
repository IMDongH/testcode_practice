package sample.cafekiosk.spring.api.service.product.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;

import javax.persistence.*;

@Getter

public class ProductResponse {


    private Long id;

    private String productNumber;

    private ProductType type;

    private ProductSellingType sellingType;

    private String name;

    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType type, ProductSellingType sellingType, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingType = sellingType;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingType(product.getSellingType())
                .build();
    }
}
