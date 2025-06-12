package com.studygroup.studygroupbackend.dto.studymember;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMemberInviteResponse extends BaseResDto {
    private Long studyId;
    private Long memberId;
    private String userName;
    private String role;
    private LocalDateTime joinedAt;

    public static StudyMemberInviteResponse fromEntity(StudyMember studyMember) {
        return StudyMemberInviteResponse.builder()
                .studyId(studyMember.getStudy().getId())
                .memberId(studyMember.getMember().getId())
                .userName(studyMember.getMember().getUserName())
                .role(studyMember.getStudyRole().name())
                .joinedAt(studyMember.getJoinedAt())
                .createdAt(studyMember.getCreatedAt())
                .modifiedAt(studyMember.getModifiedAt())
                .build();
    }
}