package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductType;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final StockRepository stockRepository;

    /**
     * 재고감소의 경우 동시성 문제가 발생할 수 있다.
     * optimistic lock / pessimistic lock /.. 등등을 고려한다.
     * @param request
     * @param currentTime
     * @return
     */
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime currentTime) {
        List<String> productNumbers = request.getProductNumber();

        List<Product> products = findProductsBy(productNumbers);

        deductStockQuantities(products);

        Order order = Order.create(products,currentTime);
        Order savedOrder = orderRepository.save(order);


        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(List<Product> products) {
        List<String> stockProductNumbers = extractStockProductNumbers(products);

        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        HashSet<String> distinctProductNumbers = new HashSet<>(stockProductNumbers);

        for (String stockProductNumber : distinctProductNumbers) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }

            //isQuantityLessThan 가 두번 사용 되지만 보장되는 것이 다르므로 두번 사용한 것.
            stock.deductQuantity(quantity);

        }
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(product -> product.getProductNumber(), p -> p));

        return productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .collect(Collectors.toList());
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        //재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        return stockMap;
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        //상품 별 counting 후 재고 차감 시도
        //key - productNumber  value - count 수
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
        return productCountingMap;
    }

    private static List<String> extractStockProductNumbers(List<Product> products) {
        //재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(product -> product.getProductNumber())
                .collect(Collectors.toList());
        return stockProductNumbers;
    }
}