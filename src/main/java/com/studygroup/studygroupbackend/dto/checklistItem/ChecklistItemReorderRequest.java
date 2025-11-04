package com.studygroup.studygroupbackend.dto.checklistItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemReorderRequest {
    private Long checklistItemId;
    private Long studyId;
    private Long studyMemberId;
    private Integer orderIndex;
}
