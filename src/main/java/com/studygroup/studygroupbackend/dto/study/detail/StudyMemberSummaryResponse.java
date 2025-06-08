package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.entity.StudyMember;
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
    private String userName;
    private String role;

    public static StudyMemberSummaryResponse fromEntity(StudyMember studyMember) {
        return StudyMemberSummaryResponse.builder()
                .id(studyMember.getMember().getId())
                .userName(studyMember.getMember().getUserName())
                .role(studyMember.getStudyRole().name())
                .build();
    }
}