package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;
import sample.cafekiosk.spring.domain.Product.ProductType;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sample.cafekiosk.spring.domain.Product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.Product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        //given
        when
                (mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        Product product1 = createProductBuild("001", HANDMADE, SELLING, "아메리카노", 1000);
        Product product2 = createProductBuild("002", HANDMADE, HOLD, "카페라떼", 2000);
        Product product3 = createProductBuild("003", HANDMADE, STOP_SELLING, "빙수", 3000);

        LocalDateTime now = LocalDateTime.of(2023, 9, 6, 00, 0);
        productRepository.saveAll(List.of(product1, product2, product3));

        List<Product> products = List.of(product1, product2, product3);
        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 9, 5, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 9, 7, 0, 0, 0), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 9, 6, 23, 59, 59), products);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 9, 6), "test@test.com");

        //then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원 입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .currentTime(now).build();
        return orderRepository.save(order);

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