package com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.entity.ChecklistItemAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudyChecklistItemDetailResponse extends BaseResDto {
    private Long checklistId;
    private String content;
    private Long memberId;
    private String memberName;
    private boolean isCompleted;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime assignedAt;
    private Integer studyOrderIndex;
    private Integer personalOrderIndex;

    public static StudyChecklistItemDetailResponse fromEntity(ChecklistItemAssignment cia) {
        return StudyChecklistItemDetailResponse.builder()
                .checklistId(cia.getChecklistItem().getId())
                .content(cia.getChecklistItem().getContent())
                .memberId(cia.getMember().getId())
                .memberName(cia.getMember().getUserName())
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