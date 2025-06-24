package com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.ChecklistItemAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemChangeStatusResponse extends BaseResDto {
    private Long checklistId;
    private Long memberId;
    private boolean isCompleted;
    private LocalDateTime completedAt;

    public static ChecklistItemChangeStatusResponse fromEntity(ChecklistItemAssignment cia) {
        return ChecklistItemChangeStatusResponse.builder()
                .checklistId(cia.getChecklistItem().getId())
                .memberId(cia.getMember().getId())
                .isCompleted(cia.isCompleted())
                .completedAt(cia.getCompletedAt())
                .createdAt(cia.getCompletedAt())
                .modifiedAt(cia.getModifiedAt())
                .build();
    }
}