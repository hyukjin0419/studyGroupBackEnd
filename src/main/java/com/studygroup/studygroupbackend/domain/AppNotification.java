package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AppNotification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    //UI 정보를 위해 필요
    @Column
    private Long referencedId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @Column
    private LocalDateTime read_at;

    public static AppNotification of(Member sender, Member recipient, NotificationType type, Long referencedId, String message) {
        return AppNotification.builder()
                .sender(sender)
                .recipient(recipient)
                .type(type)
                .referencedId(referencedId)
                .message(message)
                .isRead(false)
                .build();
    }
}
