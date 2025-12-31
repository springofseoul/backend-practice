package server.springofseoul.item.product.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Auditing 적용 (자동 관리)
// JPA가 엔티티 생명주기 이벤트(저장/수정)를 감지하여 자동으로 시간을 주입함.
import server.springofseoul.global.dao.BaseTimeEntity;

@Getter
@Entity     // @Entity 애너테이션은 Product 객체를 JPA가 관리하는 엔티티로 지정
@Builder // Builder 패턴 활성화
@AllArgsConstructor(access = AccessLevel.PROTECTED) // Builder를 위한 전체 필드 생성자 추가
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends BaseTimeEntity {

    @Id     // Id 필드를 기본 키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="item_id", updatable=false)
    private Long id;

    @Column(nullable = false, length = 100)     // null 불가능, 길이 100
    private String name;    // 물품명

    @Lob // 대용량 데이터 타입 (VARCHAR 범위를 넘는 긴 텍스트 등에 사용)
    private String imageUrl;   // 물품 이미지 링크

    @Lob
    private String description;     // 물품 상세 설명

    @Column(nullable = false)
    private int price;      // 가격

    @Column(nullable = false)
    private int stockQuantity; // 재고 수량

    // 재고 증가 / 감소 매서드
    /**
     * 재고 수량 증가
     * @param quantity 증가시킬 수량
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     * @param quantity 감소시킬 수량
     */
    public void removeStock(int quantity) {
        if (this.stockQuantity < quantity) {
            // 충분한 재고가 없을 경우 예외 처리
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }

    // 비즈니스 로직(변경 메서드)
    public void changeName(String newName) {
        this.name = newName; // Setter 대신 비즈니스 메서드를 통해 변경 (객체 지향적 접근)
    }
}