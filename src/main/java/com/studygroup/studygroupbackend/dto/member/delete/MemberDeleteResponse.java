package com.studygroup.studygroupbackend.dto.member.delete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//회원 삭제 응답 DTO
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDeleteResponse {
    private String message;

    public static MemberDeleteResponse successDelete() {
        return MemberDeleteResponse.builder()
                .message("회원 삭제 완료")
                .build();
    }
}
