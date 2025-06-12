package com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail;

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
//본인 체크리스트 조회용
public class MemberChecklistItemDetailResponse extends BaseResDto {
    private Long checklistId;
    private String content;
    private boolean isCompleted;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime assignedAt;
    private Integer studyOrderIndex;
    private Integer personalOrderIndex;

    public static MemberChecklistItemDetailResponse fromEntity(ChecklistItemAssignment cia) {
        return MemberChecklistItemDetailResponse.builder()
                .checklistId(cia.getChecklistItem().getId())
                .content(cia.getChecklistItem().getContent())
                .isCompleted(cia.isCompleted())
                .dueDate(cia.getChecklistItem().getDueDate())
                .completedAt(cia.getCompletedAt())
                .assignedAt(cia.getAssignedAt())
                .studyOrderIndex(cia.getStudyOrderIndex())
                .personalOrderIndex(cia.getPersonalOrderIndex())
                .createdAt(cia.getCompletedAt())
                .modifiedAt(cia.getModifiedAt())
                .build();
    }
}