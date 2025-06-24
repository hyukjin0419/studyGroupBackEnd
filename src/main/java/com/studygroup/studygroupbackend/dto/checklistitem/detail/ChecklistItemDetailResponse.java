package com.studygroup.studygroupbackend.dto.checklistitem.detail;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDetailResponse extends BaseResDto {
    private Long id;
    private Long creatorId;
    private Long studyId; //null 이면 개인용
    private String content;
    private LocalDateTime dueDate;

    public static ChecklistItemDetailResponse fromEntity(ChecklistItem checklistItem) {
        return ChecklistItemDetailResponse.builder()
                .id(checklistItem.getId())
                .creatorId(checklistItem.getCreator().getId())
                .studyId(checklistItem.getStudy() != null ? checklistItem.getStudy().getId() : null)
                .content(checklistItem.getContent())
                .dueDate(checklistItem.getDueDate() != null ? checklistItem.getDueDate() : null)
                .createdAt(checklistItem.getCreatedAt())
                .modifiedAt(checklistItem.getModifiedAt())
                .build();
    }
}