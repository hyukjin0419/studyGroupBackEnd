package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 기본이 되는 클래스
 * 생성 시간과 수정 시간을 자동으로 관리합니다.
 * 필드명을 DTO 클래스와 일치시키기 위해 createdAt, updatedAt으로 수정했습니다.
 * 이전에는 createAt, modifiedAt으로 되어 있어 DTO 변환 시 혼란이 있었습니다.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    /**
     * 엔티티 생성 시간
     * 한번 설정되면 변경되지 않습니다.
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 마지막 수정 시간
     * 엔티티가 수정될 때마다 자동으로 업데이트됩니다.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
