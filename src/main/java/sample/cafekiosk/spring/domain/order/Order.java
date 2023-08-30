package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.orderProduct.OrderProduct;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(List<Product> products, LocalDateTime currentTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = currentTime;
        this.orderProducts =
                products.stream()
                        .map(product -> new OrderProduct(this,product))
                        .collect(Collectors.toList());
    }

    private int calculateTotalPrice(List<Product> orderProducts) {
        return orderProducts.stream()
                .mapToInt(Product::getPrice).sum();
    }

    public static Order create(List<Product> products, LocalDateTime currentTime) {
        return new Order(products,currentTime);
    }
}
