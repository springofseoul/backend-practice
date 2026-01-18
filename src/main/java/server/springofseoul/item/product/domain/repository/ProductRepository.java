package server.springofseoul.item.product.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.springofseoul.item.product.domain.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 추후에 상품명으로 상품을 찾는 메서드 추가
}