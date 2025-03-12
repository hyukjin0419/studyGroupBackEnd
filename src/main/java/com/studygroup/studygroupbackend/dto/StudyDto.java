package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyDto {

    private Long id;
    private String name;
    private String description;
    private Long leaderId;

    public static StudyDto fromEntity(Study study) {
        return new StudyDto(
                study.getId(),
                study.getName(),
                study.getDescription(),
                study.getLeader().getId()
        );
    }
}
