package com.studygroup.studygroupbackend.dto.checklistItem;

import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistItemCreateRequest {
    private String content;
    private Long assigneeId;
    private ChecklistItemType type;
    private LocalDate targetDate;
    private Integer orderIndex;
}

