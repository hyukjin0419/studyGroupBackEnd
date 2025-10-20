package com.studygroup.studygroupbackend.dto.checklistItem;

import com.studygroup.studygroupbackend.domain.ChecklistItem;
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
public class ChecklistItemDetailResponse {
    private Long id;
    private ChecklistItemType type;
    private Long studyId;
    private Long memberId;
    private String studyName;
    private Long studyMemberId;
    private String content;
    private boolean completed;
    private LocalDate targetDate;
    private Integer orderIndex;

    public static ChecklistItemDetailResponse fromEntity(ChecklistItem item){
        return ChecklistItemDetailResponse.builder()
                .id(item.getId())
                .type(item.getType())
                .studyId(item.getStudy().getId())
                .memberId(item.getStudyMember().getMember().getId())
                .studyName(item.getStudy().getName())
                .studyMemberId(item.getStudyMember().getId())
                .content(item.getContent())
                .completed(item.isCompleted())
                .targetDate(item.getTargetDate())
                .orderIndex(item.getOrderIndex())
                .build();
    }
}
