package com.studygroup.studygroupbackend.dto.member.signup;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//회원 가입 응답 DTO
public class MemberCreateResponse extends BaseResDto {
    private Long id;

    public static MemberCreateResponse fromEntity(Member member) {
        return MemberCreateResponse.builder()
                .id(member.getId())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
