package com.studygroup.studygroupbackend.dto.study.update;


import com.studygroup.studygroupbackend.domain.status.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyUpdateRequest {
    private Long studyId;
    private String name;
    private String description;
    private String color;
    private LocalDateTime dueDate;
    private StudyStatus status;
}