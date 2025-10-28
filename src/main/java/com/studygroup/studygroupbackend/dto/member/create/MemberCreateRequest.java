package com.studygroup.studygroupbackend.dto.member.create;

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

    public Member toEntity(String encodedPassword){
        return Member.of(userName, encodedPassword, email);
    }
}