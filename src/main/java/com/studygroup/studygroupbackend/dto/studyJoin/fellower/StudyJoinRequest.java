package com.studygroup.studygroupbackend.dto.studyJoin.fellower;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyJoinRequest {
    private String joinCode;
}
