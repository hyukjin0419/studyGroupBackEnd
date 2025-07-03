package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    @Column(nullable = false)
    private String personalColor = "0xFF8AB4F8";

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    public static StudyMember of(Study study, Member member, String personalColor, StudyRole role, Integer personalOrderIndex) {
        return StudyMember.builder()
                .study(study)
                .member(member)
                .personalColor(personalColor)
                .studyRole(role)
                .personalOrderIndex(personalOrderIndex)
                .build();
    }

    public void changeRole(StudyRole newRole) {
        this.studyRole = newRole;
    }

    public void leaveStudy(){
        this.leftAt = LocalDateTime.now();
    }
}
