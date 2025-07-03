package com.studygroup.studygroupbackend.dto.study.create;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.dto.study.detail.StudyMemberSummaryResponse;
import com.studygroup.studygroupbackend.domain.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyCreateResponse extends BaseResDto {
    private Long id;
    private String name;
    private String description;
    private Long leaderId;
    private String leaderName;
    private String personalColor;

    public static StudyCreateResponse fromEntity(Study study, StudyMemberSummaryResponse leaderDto) {
        return StudyCreateResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .leaderId(leaderDto.getId())
                .leaderName(leaderDto.getUserName())
                .personalColor(leaderDto.getPersonalColor())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
