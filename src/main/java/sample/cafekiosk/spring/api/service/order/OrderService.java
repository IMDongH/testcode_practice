package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime currentTime) {
        List<String> productNumber = request.getProductNumber();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumber);

        Order order = Order.create(products,currentTime);
        Order savedOrder = orderRepository.save(order);
        log.info("------------------");
        log.info(order.toString());
        return OrderResponse.of(savedOrder);
    }
}
