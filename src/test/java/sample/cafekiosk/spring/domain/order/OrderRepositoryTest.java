package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest.*;
import static sample.cafekiosk.spring.domain.Product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.Product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest //테스트 실행 시 spring 서버를 띄워서 진행
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @DisplayName("특정 일자가 입력되면 해당 일자에 해당하는 결제 완료되ㅣㄴ 주문들을 조회한다.")
    @Test
    void findOrdersBy(){
        //given
        Product product1 = createProductBuild("001", HANDMADE, SELLING, "아메리카노", 1000);

        Stock stock1 = Stock.create("001", 100);


        productRepository.saveAll(List.of(product1));
        stockRepository.saveAll(List.of(stock1));
        LocalDateTime now = LocalDateTime.now();
        List<String> productNumbers = new ArrayList<>();

        for(int i=0; i<10; i++){
            productNumbers.add("001");
            OrderCreateServiceRequest request = builder()
                    .productNumber(productNumbers).build();




            if(i>6){ //3개만 결제 완료 상태
                OrderResponse order = orderService.createOrder(request, now,OrderStatus.PAYMENT_COMPLETED);

            }else{
                OrderResponse order = orderService.createOrder(request, now,OrderStatus.INIT);

            }
        }

        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            System.out.println(order.toString());
        }
        //when
        List<Order> orders = orderRepository.findOrdersBy(now.toLocalDate().atStartOfDay(), now.toLocalDate().plusDays(1).atStartOfDay(), OrderStatus.PAYMENT_COMPLETED);

        //then
        assertThat(orders).hasSize(3)
                .extracting("totalPrice","orderStatus")
                .containsExactlyInAnyOrder(
                        tuple(8000,OrderStatus.PAYMENT_COMPLETED),
                        tuple(9000,OrderStatus.PAYMENT_COMPLETED),
                        tuple(10000,OrderStatus.PAYMENT_COMPLETED)
                );
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