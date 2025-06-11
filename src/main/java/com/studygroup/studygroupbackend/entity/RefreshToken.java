package com.studygroup.studygroupbackend.entity;

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
    private String userName;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public RefreshToken(String userName, String token, LocalDateTime expiresAt){
        this.userName = userName;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public void updateToken(String newToken, LocalDateTime newExpiry) {
        this.token = newToken;
        this.expiresAt = newExpiry;
    }
}
