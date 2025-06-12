package com.studygroup.studygroupbackend.security.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {
    private String accessToken;
}