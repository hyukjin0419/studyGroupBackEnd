package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.StudyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "studies")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String color = "0xFF8AB4F8";

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = true)
    private Double progress;

    @Column(nullable = false)
    private StudyStatus status = StudyStatus.PROGRESSING;

    @Column(nullable = false, unique = true, updatable = false)
    private String joinCode;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @PrePersist
    private void init() {
        if (this.joinCode == null){
            this.joinCode = UUID.randomUUID().toString();
        }
        this.deleted = false;
    }

    public static Study of(
            String name,
            String description,
            String color,
            LocalDateTime dueDate,
            Double progress,
            StudyStatus status
            ) {
        return Study.builder()
                .name(name)
                .description(description)
                .color(color != null ? color : "0xFF8AB4F8")
                .dueDate(dueDate != null ? dueDate : LocalDateTime.now().plusDays(30))
                .progress(progress != null ? progress : 0.0)
                .status(status != null ? status : StudyStatus.PROGRESSING)
                .build();
    }

    public void updateStudyInfo(
            String name,
            String description,
            String color,
            LocalDateTime dueDate,
            StudyStatus status
    ) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (color != null) this.color = color;
        if (dueDate != null) this.dueDate = dueDate;
        if (status != null) this.status = status;
    }

    public void softDeletion(){
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
