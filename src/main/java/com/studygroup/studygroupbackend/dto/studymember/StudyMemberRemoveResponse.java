package com.studygroup.studygroupbackend.dto.studymember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMemberRemoveResponse {
    private Long studyId;
    private Long memberId;
    private String message;

    public static StudyMemberRemoveResponse successDelete(Long studyId, Long memberId) {
        return StudyMemberRemoveResponse.builder()
                .studyId(studyId)
                .memberId(memberId)
                .message("스터디에서 멤버가 삭제되었습니다.")
                .build();
    }
}