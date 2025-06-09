package com.studygroup.studygroupbackend.dto.checklistitem.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemUpdateDueDateRequest {
    private LocalDateTime dueDate;
}
