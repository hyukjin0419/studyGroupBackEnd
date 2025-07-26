package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "study_invitation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StudyInvitation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private Member inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private Member invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private String message;

    @Column
    private LocalDate respondedAt;

    public static StudyInvitation of(
            Study study, Member inviter, Member invitee, InvitationStatus status
    ) {
        return StudyInvitation.builder()
                .study(study)
                .inviter(inviter)
                .invitee(invitee)
                .status(status != null ? status : InvitationStatus.PENDING)
                .build();
    }
}
