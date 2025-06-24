package com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign;

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
public class ChecklistItemAssignResponse extends BaseResDto {
    private Long checklistId;
    private Long memberId;
    private LocalDateTime assignedAt;

    public static ChecklistItemAssignResponse fromEntity(ChecklistItemAssignment cia) {
        return ChecklistItemAssignResponse.builder()
                .checklistId(cia.getChecklistItem().getId())
                .memberId(cia.getMember().getId())
                .assignedAt(cia.getAssignedAt())
                .createdAt(cia.getCreatedAt())
                .modifiedAt(cia.getModifiedAt())
                .build();
    }
}