package com.studygroup.studygroupbackend.dto.checklistitem.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemCreateRequest {
    private Long studyId; //null -> 개인용
    private String content;
    private LocalDateTime dueDate;
}
