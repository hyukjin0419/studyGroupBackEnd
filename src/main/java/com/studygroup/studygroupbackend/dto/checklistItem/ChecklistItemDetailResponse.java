package com.studygroup.studygroupbackend.dto.checklistItem;

import com.studygroup.studygroupbackend.domain.ChecklistItem;
import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemDetailResponse {
    private Long id;
    private ChecklistItemType type;
    private Long studyId;
    private Long studyMemberId;
    private String content;
    private boolean completed;
    private Integer orderIndex;

    public static ChecklistItemDetailResponse fromEntity(ChecklistItem item){
        return ChecklistItemDetailResponse.builder()
                .id(item.getId())
                .type(item.getType())
                .studyId(item.getStudy().getId())
                .studyMemberId(item.getStudyMember().getId())
                .content(item.getContent())
                .completed(item.isCompleted())
                .orderIndex(item.getOrderIndex())
                .build();
    }
}
