package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.StudyStatus;
import com.studygroup.studygroupbackend.domain.superEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "studies")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
public class Study extends SoftDeletableEntity {

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyStatus status = StudyStatus.PROGRESSING;

    @Column(nullable = false, unique = true, updatable = false, length = 191)
    private String joinCode;

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
            StudyStatus status
            ) {
        return Study.builder()
                .name(name)
                .description(description)
                .color(color != null ? color : "0xFF8AB4F8")
                .dueDate(dueDate)
                .status(status != null ? status : StudyStatus.PROGRESSING)
                .deleted(false)
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

    public void completeStudy(){
        this.status = StudyStatus.DONE;
    }

    public void unCompleteStudy(){
        this.status = StudyStatus.PROGRESSING;
    }
}
