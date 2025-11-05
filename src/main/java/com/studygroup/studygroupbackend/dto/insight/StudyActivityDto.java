package com.studygroup.studygroupbackend.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyActivityDto {
    private Long studyId;
    private String studyName;
    private double activityRate; // 0~1
}
