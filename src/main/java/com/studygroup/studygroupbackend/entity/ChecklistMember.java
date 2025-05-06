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

    private ChecklistMember(Checklist checklist, Member member, LocalDateTime assignedAt) {
        this.checklist = checklist;
        this.member = member;
        this.assignedAt = assignedAt;
    }

    public static ChecklistMember assign(Checklist checklist, Member member) {
        return new ChecklistMember(checklist, member, LocalDateTime.now());
    }

    public void changeStatus() {
        this.isCompleted = !this.isCompleted;
        this.completedAt = this.isCompleted ? LocalDateTime.now() : null;
    }

}
