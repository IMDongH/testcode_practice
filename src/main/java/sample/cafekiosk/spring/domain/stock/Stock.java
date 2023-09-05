package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity; //수량

    @Builder
    public Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }


    public static Stock create(String productNumber, int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();

    }

    public boolean isQuantityLessThan(int quantity) {
        return this.quantity<quantity;
    }

    public void deductQuantity(int quantity) { //수량을 차감할 때 올바른 차감 로직이 수행되도록 예외를 던져야한다.
        if(isQuantityLessThan(quantity)){
            throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
        }
        this.quantity = this.quantity-quantity;
    }
}
