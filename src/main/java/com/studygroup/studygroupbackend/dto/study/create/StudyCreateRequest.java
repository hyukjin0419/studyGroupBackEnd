package com.studygroup.studygroupbackend.dto.study.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyCreateRequest{
    private String name;
    private String description;
    private Long leaderId;
}
