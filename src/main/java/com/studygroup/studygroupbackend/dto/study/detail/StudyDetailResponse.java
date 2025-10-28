package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.status.StudyStatus;
import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyDetailResponse extends BaseResDto {
    private Long id;
    private String name;
    private String description;
    private Long leaderId;
    private String leaderName;
    private String joinCode;
    private String personalColor;
    private LocalDateTime dueDate;
    private Double progress;
    private StudyStatus status;
    private Integer personalOrderIndex;
    private List<StudyMemberSummaryResponse> members;

    public static StudyDetailResponse fromEntity(
            Study study,
            StudyMember studyMember,
            StudyMemberSummaryResponse leaderDto,
            List<StudyMemberSummaryResponse> members
    ) {
        return StudyDetailResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .leaderId(leaderDto.getId())
                .leaderName(leaderDto.getDisplayName())
                .joinCode(study.getJoinCode())
                .members(members == null ? List.of() : members)
                .personalColor(studyMember.getPersonalColor())
                .dueDate(study.getDueDate())
                .progress(study.getProgress())
                .status(study.getStatus())
                .personalOrderIndex(studyMember.getPersonalOrderIndex())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
