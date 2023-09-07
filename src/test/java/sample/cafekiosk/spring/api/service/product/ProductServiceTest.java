package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.Product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.Product.ProductType.HANDMADE;


class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct(){
        //given
        Product product1 = createProductBuild("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProductBuild("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProductBuild("003", HANDMADE, STOP_SELLING, "빙수", 7000);
        productRepository.saveAll(List.of(product1,product2,product3));

        ProductCreateRequest request = createProductRequest(HANDMADE, SELLING, "카푸치노", 5000);

        //when
        ProductResponse response = productService.createProduct(request.toServiceRequest());

        //then
        assertThat(response)
                .extracting("productNumber","type","sellingType","name","price")
                .contains("004",HANDMADE,SELLING,"카푸치노",5000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(4)
                .extracting("productNumber","type","sellingType","name","price")
                .containsExactlyInAnyOrder(
                        tuple("001",HANDMADE,SELLING,"아메리카노",4000),
                        tuple("002",HANDMADE,HOLD,"카페라떼",4500),
                        tuple("003",HANDMADE,STOP_SELLING,"빙수",7000),
                        tuple("004",HANDMADE,SELLING,"카푸치노",5000)
                );
    }


    @DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다. 처음인 경우 001")
    @Test
    void createProductWithInit(){
        //given

        ProductCreateRequest request = createProductRequest(HANDMADE, SELLING, "카푸치노", 5000);

        //when
        ProductResponse response = productService.createProduct(request.toServiceRequest());

        //then
        assertThat(response)
                .extracting("productNumber","type","sellingType","name","price")
                .contains("001",HANDMADE,SELLING,"카푸치노",5000);
    }

    private static ProductCreateRequest createProductRequest(ProductType type, ProductSellingType sellingType, String name, int price) {
        return ProductCreateRequest.builder()
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price).build();
    }

    private Product createProductBuild(String productNumber, ProductType type, ProductSellingType sellingType, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingType(sellingType)
                .name(name)
                .price(price)
                .build();
    }


}