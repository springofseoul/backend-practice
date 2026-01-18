package server.springofseoul.item.product.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.springofseoul.global.response.ApiResponse;
import server.springofseoul.global.response.status.SuccessStatus;
import server.springofseoul.item.product.api.request.ProductRequestDto;
import server.springofseoul.item.product.application.response.ProductListResponseDto;
import server.springofseoul.item.product.application.response.ProductResponseDto;
import server.springofseoul.item.product.domain.entity.Product;
import server.springofseoul.item.product.application.ProductService;

import java.util.List;

@RestController // REST API 컨트롤러임을 명시
@RequestMapping("/api/v1/products") // 기본 URL 경로 설정 (예: http://localhost:8080/api/v1/products)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. 상품 등록 API (CREATE)
    // HTTP Method: POST
    // URL: /api/v1/products
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> create(@RequestBody ProductRequestDto req) {
        ProductResponseDto response = productService.createProduct(req);
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }

    // 2. 상품 단건 조회 API
    // HTTP Method: GET
    // URL: /api/v1/products/{id}
    @GetMapping("/{product_id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(@PathVariable("product_id") Long id) {
        Product product = productService.getProduct(id);
        ProductResponseDto response = ProductResponseDto.of(product);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 3. 상품 전체 조회 API
    // HTTP Method: GET
    // URL: /api/v1/products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductListResponseDto>>> getProducts() {
        List<Product> products = productService.getProducts();
        List<ProductListResponseDto> response = products.stream()
                .map(ProductListResponseDto::of)
                .toList();
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 4. 상품 정보 수정 API (UPDATE: 이름 변경 예시)
    // HTTP Method: PATCH
    // URL: /api/v1/products/{id}
    @PatchMapping("/{product_id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> changeName(
            @PathVariable Long id,
            @RequestBody ProductRequestDto request  // DTO 사용
    ) {
        // Service에서 이름 변경 (changeName 메서드는 ProductService에 정의되어 있어야 함)
        ProductResponseDto response = productService.changeName(id, request.getName());
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    // 5. 상품 삭제 API
    // HTTP Method: DELETE
    // URL: /api/v1/products/{id}
    @DeleteMapping("/{product_id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("product_id") Long id) {
        productService.deleteProduct(id);
        // 204 No Content 응답 (본문에 데이터 없음)
        return ApiResponse.onSuccess(SuccessStatus.OK);
    }
}

// 정적 메서드랑 제너릭 타입, 상속(예외 처리) 구조가 어떻게 돼있는지 illdan, 사이드프로젝트