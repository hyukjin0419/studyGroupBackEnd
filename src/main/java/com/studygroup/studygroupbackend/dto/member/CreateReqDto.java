package com.studygroup.studygroupbackend.dto.member;

import com.studygroup.studygroupbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReqDto {
    private String userName;
    private String password;
    private String email;

    public Member toEntity(){
        return Member.of(userName, password, email);
    }
}