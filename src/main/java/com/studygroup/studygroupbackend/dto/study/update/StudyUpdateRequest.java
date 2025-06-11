package com.studygroup.studygroupbackend.dto.study.update;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyUpdateRequest {
    private String name;
    private String description;
}