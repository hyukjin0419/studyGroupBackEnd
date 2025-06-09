package com.studygroup.studygroupbackend.dto.checklistitem.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemUpdateContentRequest {
    private String content;
}
