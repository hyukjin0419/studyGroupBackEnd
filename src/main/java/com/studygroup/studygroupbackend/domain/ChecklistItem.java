package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.superEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChecklistItem extends BaseEntity {

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
    private LocalDateTime dueDate; //null이 기본값

    private ChecklistItem(Study study, Member creator, String content){
        this(study, creator, content, null);
    }

    private ChecklistItem(Study study, Member creator, String content, LocalDateTime dueDate){
        this.study = study;
        this.creator = creator;
        this.content = content;
        this.dueDate = dueDate;
    }

    public static ChecklistItem of(Study study, Member creator, String content) {
        return new ChecklistItem(study, creator, content);
    }

    public static ChecklistItem of(Study study, Member creator, String content, LocalDateTime dueDate) {
        return new ChecklistItem(study, creator, content, dueDate);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}