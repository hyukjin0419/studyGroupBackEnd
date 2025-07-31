package com.studygroup.studygroupbackend.dto.member.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLogoutRequest {
    private String deviceToken;
}
