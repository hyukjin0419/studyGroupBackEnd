package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Checklist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDto {

    private Long id;
    private Long studyId;
    private Long memberId;
    private LocalDate date;
    private String content;
    private boolean isChecked;
    private LocalDateTime createdAt;

    public static ChecklistDto fromEntity(Checklist checklist) {
        return new ChecklistDto(
                checklist.getId(),
                checklist.getStudy().getId(),
                checklist.getMember() != null ? checklist.getMember().getId() : null,
                checklist.getDate(),
                checklist.getContent(),
                checklist.isChecked(),
                checklist.getCheckedAt()
        );
    }
}
