package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    private List<StudyMemberSummaryResponse> members;

    public static StudyDetailResponse fromEntity(Study study, StudyMemberSummaryResponse leaderDto, List<StudyMemberSummaryResponse> members) {
        return StudyDetailResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .leaderId(leaderDto.getId())
                .leaderName(leaderDto.getUserName())
                .members(members)
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
