package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private StudyMember(Study study, Member member, StudyRole studyRole) {
        this.study = study;
        this.member = member;
        this.studyRole = studyRole;
    }

    public static StudyMember of(Study study, Member member, StudyRole role) {
        return new StudyMember(study, member, role);
    }

    public void changeRole(StudyRole newRole) {
        this.studyRole = newRole;
    }
}
