package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_invitation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("deleted = false")
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
    private LocalDateTime respondedAt;

    @Column(nullable = false)
    @Builder.Default
    boolean deleted = false;

    @Column(nullable = true)
    LocalDateTime deletedAt;

    public static StudyInvitation of(
            Study study, Member inviter, Member invitee, InvitationStatus status, String message
    ) {
        return StudyInvitation.builder()
                .study(study)
                .inviter(inviter)
                .invitee(invitee)
                .message(message)
                .status(status != null ? status : InvitationStatus.PENDING)
                .build();
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void decline(){
        this.status = InvitationStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void softDeletion(){
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
