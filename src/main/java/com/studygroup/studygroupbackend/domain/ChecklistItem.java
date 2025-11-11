package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
import com.studygroup.studygroupbackend.domain.superEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_items")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
//@Check(constraints = "(" +
//        "(type = 'STUDY' AND study_id IS NOT NULL AND member_id IS NULL) OR " +
//        "(type = 'PERSONAL' AND member_id IS NOT NULL AND study_id IS NULL)" +
//        ")")
//TODO: 개인용 팀용 체크리스트 분리하기
public class ChecklistItem extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Enumerated(EnumType.STRING)
//    private ChecklistItemType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    public static ChecklistItem ofGroup(
            Study study,
            Member creator,
            StudyMember assignee,
            String content,
            Integer orderIndex,
            LocalDate targetDate,
            LocalDateTime dueDate
    ) {
        return ChecklistItem.builder()
//                .type(ChecklistItemType.STUDY)
                .study(study)
                .creator(creator)
                .studyMember(assignee)
                .content(content)
                .targetDate(targetDate)
                .orderIndex(orderIndex)
                .dueDate(dueDate)
                .completed(false)
                .completedAt(null)
                .deleted(false)
                .build();
    }

    public static ChecklistItem ofPersonal(
            Member creator,
            String content,
            LocalDateTime dueDate
    ) {
        return ChecklistItem.builder()
//                .type(ChecklistItemType.PERSONAL)
                .creator(creator)
                .content(content)
                .dueDate(dueDate)
                .deleted(false)
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(boolean status) {
        this.completed = status;
    }

    public void updateOrderIndex(int orderIndex){
        this.orderIndex = orderIndex;
    }

    public void updateStudy(Study study) {this.study = study; }

    public void updateStudyMemberId(StudyMember studyMember){
        this.studyMember = studyMember;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}