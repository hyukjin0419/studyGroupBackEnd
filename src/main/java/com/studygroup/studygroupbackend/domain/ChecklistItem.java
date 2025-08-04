package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.superEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_items")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
public class ChecklistItem extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = true)
    private LocalDateTime dueDate;

    public static ChecklistItem of(Study study, Member creator, String content, LocalDateTime dueDate) {
        return ChecklistItem.builder()
                .study(study)
                .creator(creator)
                .content(content)
                .dueDate(dueDate)
                .deleted(false)
                .build();
    }


    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}