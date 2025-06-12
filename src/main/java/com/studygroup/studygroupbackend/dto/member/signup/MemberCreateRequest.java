package com.studygroup.studygroupbackend.dto.member.signup;

import com.studygroup.studygroupbackend.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

// 회원 가입 요청 DT

public class MemberCreateRequest {
    private String userName;
    private String password;
    private String email;

    public Member toEntity(){
        return Member.of(userName, password, email);
    }
}