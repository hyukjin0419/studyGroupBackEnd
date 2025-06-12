package com.studygroup.studygroupbackend.dto.checklistitem.create;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.domain.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemCreateResponse extends BaseResDto {
    private Long checklistItemId;

    public static ChecklistItemCreateResponse fromEntity(ChecklistItem checklistItem) {
        return ChecklistItemCreateResponse.builder()
                .checklistItemId(checklistItem.getId())
                .createdAt(checklistItem.getCreatedAt())
                .modifiedAt(checklistItem.getModifiedAt())
                .build();
    }
}