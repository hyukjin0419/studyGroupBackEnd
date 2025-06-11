package com.studygroup.studygroupbackend.dto.checklistItemAssignment.unassign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemUnassignResponse{
    private Long checklistId;
    private Long memberId;
    private String message;

    public static ChecklistItemUnassignResponse success(Long checklistId, Long memberId) {
        return ChecklistItemUnassignResponse.builder()
                .checklistId(checklistId)
                .memberId(memberId)
                .message("체크리스트 할당 해제 완료")
                .build();
    }
}