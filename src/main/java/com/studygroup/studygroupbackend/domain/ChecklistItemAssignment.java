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
@Table(name = "checklist_item_assignments")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLRestriction("deleted = false")
public class ChecklistItemAssignment extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_item_id", nullable = false)
    private ChecklistItem checklistItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    private StudyMember studyMember;

    @Column(nullable = false)
    private boolean completed = false;

    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private int studyOrderIndex;

    @Column(nullable = false)
    private int personalOrderIndex;


    public static ChecklistItemAssignment assign(
            StudyMember studyMember,
            ChecklistItem checklistItem,
            int studyOrderIndex,
            int personalOrderIndex)
    {
        return ChecklistItemAssignment.builder()
                .studyMember(studyMember)
                .checklistItem(checklistItem)
                .assignedAt(LocalDateTime.now())
                .completed(false)
                .deleted(false)
                .studyOrderIndex(studyOrderIndex)
                .personalOrderIndex(personalOrderIndex)
                .build();
    }

    public void changeStatus() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now() : null;
    }

    public void updateStudyOrderIndex(int newIndex){
        this.studyOrderIndex = newIndex;
    }

    public void updatePersonalOrderIndex(int newIndex){
        this.personalOrderIndex = newIndex;
    }

}
