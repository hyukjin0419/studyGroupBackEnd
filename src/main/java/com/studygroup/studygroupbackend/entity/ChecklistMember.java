package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklist_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChecklistMember extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

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

    private ChecklistMember(Checklist checklist, Member member, Study study, LocalDateTime assignedAt,
                            int studyOrderIndex, int personalOrderIndex) {
        this.checklist = checklist;
        this.member = member;
        this.assignedAt = assignedAt;
        this.studyOrderIndex = studyOrderIndex;
        this.personalOrderIndex = personalOrderIndex;
    }

    public static ChecklistMember assign(Checklist checklist, Member member, Study study,
                                         int studyOrderIndex, int personalOrderIndex) {
        return new ChecklistMember(
                checklist,
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
