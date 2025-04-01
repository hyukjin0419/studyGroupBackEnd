package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "checklists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist extends BaseEntity {

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

    @Column
    private LocalDateTime dueDate; //null이 기본값

    private Checklist(Study study, Member creator, String content){
        this(study, creator, content, null);
    }

    private Checklist(Study study, Member creator, String content, LocalDateTime dueDate){
        this.study = study;
        this.creator = creator;
        this.content = content;
        this.dueDate = dueDate;
    }

    public static Checklist of(Study study, Member creator, String content) {
        return new Checklist(study, creator, content);
    }

    public static Checklist of(Study study, Member creator, String content, LocalDateTime dueDate) {
        return new Checklist(study, creator, content, dueDate);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}