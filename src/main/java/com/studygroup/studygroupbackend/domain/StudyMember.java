package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.StudyMemberStatus;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
import com.studygroup.studygroupbackend.domain.superEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_members",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_study_member",
                columnNames = {"study_id", "member_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@SQLRestriction("deleted = false")
public class StudyMember extends SoftDeletableEntity {

    private static final Logger log = LoggerFactory.getLogger(StudyMember.class);
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
    @Column(nullable = false)
    private StudyRole studyRole;

    @Column(nullable = false)
    private Integer personalOrderIndex;

    @Column(nullable = false)
    private String personalColor = "0xFF8AB4F8";

    private LocalDateTime joinedAt;

    private LocalDateTime leftAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StudyMemberStatus studyMemberStatus = StudyMemberStatus.ACTIVE;


    //============== 생성자 메소드 ==========================

    public static StudyMember of(Study study, Member member, String personalColor, StudyRole role, Integer personalOrderIndex, LocalDateTime joinedAt) {
        return StudyMember.builder()
                .study(study)
                .member(member)
                .personalColor(personalColor)
                .studyRole(role)
                .joinedAt(joinedAt)
                .personalOrderIndex(personalOrderIndex)
                .deleted(false)
                .build();
    }

    //============== 비지니스 메소드 ===============
    public void updatePersonalColor(String personalColor){
        this.personalColor = personalColor;
    }

    public void changeRole(StudyRole newRole) {
        this.studyRole = newRole;
    }
}
