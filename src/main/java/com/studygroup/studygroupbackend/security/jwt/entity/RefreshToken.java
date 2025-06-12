package com.studygroup.studygroupbackend.security.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private Long memberId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public RefreshToken(Long memberId, String token, LocalDateTime expiresAt){
        this.memberId = memberId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public void updateToken(String newToken, LocalDateTime newExpiry) {
        this.token = newToken;
        this.expiresAt = newExpiry;
    }
}
