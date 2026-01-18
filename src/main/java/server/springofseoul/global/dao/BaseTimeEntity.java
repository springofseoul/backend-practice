package server.springofseoul.global.dao;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 테이블로 매핑되지 않고, 자식 엔티티에게 매핑 정보만 제공
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 이벤트 리스너 지정
public abstract class BaseTimeEntity {

    @CreatedDate                            // 엔티티 생성 시 시간이 자동 저장
    @Column(updatable = false)              // 한번 저장되면 이후 수정 불가
    private LocalDateTime createdDate;

    @LastModifiedDate                       // 엔티티 수정 시 시간이 자동 저장/업데이트
    private LocalDateTime lastModifiedDate;
}