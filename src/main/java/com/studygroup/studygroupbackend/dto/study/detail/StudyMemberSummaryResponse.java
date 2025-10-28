package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.domain.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMemberSummaryResponse {
    private Long id;
    private Long memberId;
    private Long studyMemberId;
    private String displayName;
    private String userName;
    private String role;
    private String personalColor;

    public static StudyMemberSummaryResponse fromEntity(StudyMember studyMember) {
        return StudyMemberSummaryResponse.builder()
                .id(studyMember.getMember().getId())
                .studyMemberId(studyMember.getId())
                .displayName(studyMember.getMember().getDisplayName())
                .memberId(studyMember.getMember().getId())
                .userName(studyMember.getMember().getUserName())
                .role(studyMember.getStudyRole().name())
                .personalColor(studyMember.getPersonalColor())
                .build();
    }
}