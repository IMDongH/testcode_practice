package sample.cafekiosk.spring.domain.Product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.Product.ProductSellingType.*;
import static sample.cafekiosk.spring.domain.Product.ProductType.*;

@ActiveProfiles("test")
//@SpringBootTest //테스트 실행 시 spring 서버를 띄워서 진행
@DataJpaTest //Spring 서버를 띄워서 진행하는 건 같음 하지만 jpa 관련 bean 만 주입하기 때문에 더 빠름 - transactional 이 달려 있어서 자동으로 rollback 이 되었다.
class ProductRepositoryTest {
    /**
     * 단위 테스트 성격을 가지고 있긴함
     */
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상품을 가진 상품들을 조회한다.") //문장 단위로 할 것
    @Test
    void findAllBySellingTypeIn(){
        //given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingType(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingType(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllBySellingTypeIn(List.of(SELLING, HOLD));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingType")
//                .containsExactly() //순서까지 맞는지 확인
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)
                ); //순서 상관 없이

    }

    @DisplayName("상품 번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn(){
        //given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingType(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();
        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingType(HOLD)
                .name("카페라떼")
                .price(4500)
                .build();
        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingType(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber","name","sellingType")
//                .containsExactly() //순서까지 맞는지 확인
                .containsExactlyInAnyOrder(
                        tuple("001","아메리카노",SELLING),
                        tuple("002","카페라떼",HOLD)
                ); //순서 상관 없이
    }

}
