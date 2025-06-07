package com.studygroup.studygroupbackend.dto.member.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//로그인 응답 DTO
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponse {
    private Long id;
    private String userName;
}
