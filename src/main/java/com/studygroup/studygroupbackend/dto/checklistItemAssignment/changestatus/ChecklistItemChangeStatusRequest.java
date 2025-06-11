package com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemChangeStatusRequest{
    private Long checklistId;
    private Long memberId;
}