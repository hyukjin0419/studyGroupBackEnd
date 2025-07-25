package com.studygroup.studygroupbackend.dto.member.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//로그인 요청 DTO
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequest {
    private String userName;
    private String password;
    private String deviceToken;
    private String deviceType;
}