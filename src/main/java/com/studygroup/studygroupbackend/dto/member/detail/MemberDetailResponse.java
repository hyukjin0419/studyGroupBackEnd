package com.studygroup.studygroupbackend.dto.member.detail;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//회원 상세 조회 응답 DTO
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailResponse extends BaseResDto {
    private Long id;
    private String userName;
    private String email;

    public static MemberDetailResponse fromEntity(Member member) {
        return MemberDetailResponse.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
