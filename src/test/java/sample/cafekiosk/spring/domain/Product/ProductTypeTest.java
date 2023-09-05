package sample.cafekiosk.spring.domain.Product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTyp1(){
        //given
        ProductType handmade = ProductType.HANDMADE;

        //when
        boolean result = ProductType.containsStockType(handmade);

        //then
        assertThat(result).isFalse();
    }


    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2(){
        //given
        ProductType bakery = ProductType.BAKERY;

        //when
        boolean result = ProductType.containsStockType(bakery);

        //then
        assertThat(result).isTrue();
    }

}