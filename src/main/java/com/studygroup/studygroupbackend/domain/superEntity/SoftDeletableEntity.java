package com.studygroup.studygroupbackend.domain.superEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false") //상속해도 적용안됨. 자식 클래스에 적용 바람.
public class SoftDeletableEntity extends BaseEntity {

    @Column(nullable = false)
    protected boolean deleted = false;

    @Column(nullable = true)
    protected LocalDateTime deletedAt;

    public void softDeletion() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deleted;
    }
}
