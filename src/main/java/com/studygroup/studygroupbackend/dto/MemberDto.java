package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userName;
    private String email;

    public static MemberDto formEntity(Member member) {
        return MemberDto.builder
    }
}
