package com.studygroup.studygroupbackend.dto.study.update;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.dto.study.detail.StudyMemberSummaryResponse;
import com.studygroup.studygroupbackend.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyUpdateResponse extends BaseResDto {
    private Long id;
    private String name;
    private String description;
    private Long leaderId;
    private String leaderName;

    public static StudyUpdateResponse fromEntity(Study study, StudyMemberSummaryResponse leaderDto) {
        return StudyUpdateResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .leaderId(leaderDto.getId())
                .leaderName(leaderDto.getUserName())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}