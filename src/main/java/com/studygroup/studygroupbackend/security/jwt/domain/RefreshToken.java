package com.studygroup.studygroupbackend.security.jwt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false,unique = true)
    private String token;

    @Column(nullable = false)
    private boolean expired = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


    //이거 of로 만들어야 할 듯.
    @Builder
    public RefreshToken(Long memberId, String token, LocalDateTime expiresAt){
        this.memberId = memberId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void updateToken(String newToken, LocalDateTime newExpiry) {
        this.token = newToken;
        this.expiresAt = newExpiry;
        this.expired = false;
        this.createdAt = LocalDateTime.now();
    }

    public void expire() {
        this.expired = true;
    }
}
