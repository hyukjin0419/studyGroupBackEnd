package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String uesrName;
    private String email;

    public static MemberDto formEntity(Member member) {
        return new MemberDto(member.getId(), member.getUserName(), member.getEmail());
    }
}
