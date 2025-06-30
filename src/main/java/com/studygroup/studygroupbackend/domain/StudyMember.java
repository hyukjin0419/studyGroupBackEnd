package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private StudyRole studyRole;

    @Column(nullable = false)
    private Integer personalOrderIndex;

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    private StudyMember(Study study, Member member, StudyRole studyRole, Integer personalOrderIndex, LocalDateTime joinedAt) {
        this.study = study;
        this.member = member;
        this.personalOrderIndex = personalOrderIndex;
        this.studyRole = studyRole;
        this.joinedAt = joinedAt;
    }

    public static StudyMember of(Study study, Member member, StudyRole role, Integer personalOrderIndex) {
        return new StudyMember(study, member, role, personalOrderIndex, LocalDateTime.now());
    }

    public void changeRole(StudyRole newRole) {
        this.studyRole = newRole;
    }

    public void leaveStudy(){
        this.leftAt = LocalDateTime.now();
    }
}
