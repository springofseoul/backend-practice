package server.springofseoul.item.product.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 내가 만든 데이터 구조, Request, Repository
import server.springofseoul.global.exception.CustomException;
import server.springofseoul.item.product.application.response.ProductResponseDto;
import server.springofseoul.item.product.domain.entity.Product;
import server.springofseoul.item.product.api.request.ProductRequestDto;
import server.springofseoul.item.product.domain.repository.ProductRepository;
import server.springofseoul.item.product.status.ProductErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 클래스 전체에 읽기 전용 트랜잭션 적용 (조회 성능 최적화)
public class ProductService {

    private final ProductRepository productRepository;

    // CREATE
    @Transactional  // 메서드 레벨 쓰기/수정/삭제 작업에는 별도로 트랜잭션을 설정하여 데이터 변경을 허용
    public ProductResponseDto createProduct(ProductRequestDto request) {

        // DTO를 사용하여 Builder 패턴으로 Product 엔티티 생성
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .build();

        // DB에 저장하고 Dto 형식으로 반환
        Product saved = productRepository.save(product);
        return ProductResponseDto.of(saved);
    }

    // READ: 단건 조회
    @Transactional(readOnly = true)
    public Product getProduct(Long productid) {
        return productRepository.findById(productid)
                .orElseThrow(() -> new CustomException(ProductErrorStatus._INVALID_PRODUCT));
    }

    // READ: 전체 조회
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // UPDATE
    @Transactional
    public ProductResponseDto changeName(Long id, String newName) {
        Product product = getProduct(id);   // 영속 상태 엔티티
        product.changeName(newName);      // 변경 감지로 UPDATE 수행
        return ProductResponseDto.of(product);
    }

    // DELETE
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }
}