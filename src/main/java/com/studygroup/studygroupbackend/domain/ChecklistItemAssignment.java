package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_item_assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChecklistItemAssignment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklistItem_id", nullable = false)
    private ChecklistItem checklistItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean isCompleted = false;

    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private int studyOrderIndex;

    @Column(nullable = false)
    private int personalOrderIndex;

    private ChecklistItemAssignment(ChecklistItem checklistItem, Member member, Study study, LocalDateTime assignedAt,
                            int studyOrderIndex, int personalOrderIndex) {
        this.checklistItem = checklistItem;
        this.member = member;
        this.assignedAt = assignedAt;
        this.studyOrderIndex = studyOrderIndex;
        this.personalOrderIndex = personalOrderIndex;
    }

    public static ChecklistItemAssignment assign(ChecklistItem checklistItem, Member member, Study study,
                                         int studyOrderIndex, int personalOrderIndex) {
        return new ChecklistItemAssignment(
                checklistItem,
                member,
                study,
                LocalDateTime.now(),
                studyOrderIndex,
                personalOrderIndex
        );
    }

    public void changeStatus() {
        this.isCompleted = !this.isCompleted;
        this.completedAt = this.isCompleted ? LocalDateTime.now() : null;
    }

    public void updateStudyOrderIndex(int newIndex){
        this.studyOrderIndex = newIndex;
    }

    public void updatePersonalOrderIndex(int newIndex){
        this.personalOrderIndex = newIndex;
    }

}
