package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.Product.Product;
import sample.cafekiosk.spring.domain.Product.ProductRepository;
import sample.cafekiosk.spring.domain.Product.ProductSellingType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        //productNumber 부여
        //ex) 001 002 003
        //DB에서 마지막 저장된 product 의 상품번호를 읽어와서 +1
        String nextProductNumber = createNextProductNumber(productRepository.findLatestProductNumber());
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);

    }

    private String createNextProductNumber(String productNumber) {

        if(productNumber==null){
            return "001";
        }
        int latestProductNumberInt= Integer.parseInt(productNumber);
        int nextProductInt = latestProductNumberInt + 1;

        return String.format("%03d",nextProductInt);
    }


    public List<ProductResponse> getSellingProducts(){
        List<Product> productList = productRepository.findAllBySellingTypeIn(ProductSellingType.forDisplay());
        return productList.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

}
