package com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemAssignRequest {
    private Long checklistId;
    private Long memberId;
    private Long studyId;
}