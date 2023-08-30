package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.Product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.Product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();


        Product product1 =
                createProduct(HANDMADE, "001", 1000);
        Product product2 =
                createProduct(HANDMADE, "002", 3000);
        Product product3 =
                createProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumber(List.of("001", "002"))
                .build();

        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(registeredDateTime,4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000));
    }

    private Product createProduct(ProductType productType, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .name("팥빙수")
                .price(price)
                .sellingType(SELLING)
                .name("메뉴 이름")
                .build();
    }
}