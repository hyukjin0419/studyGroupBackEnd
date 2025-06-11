package com.studygroup.studygroupbackend.dto.checklistitem.delete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//삭제 추가
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDeleteResponse{
    private Long checklistItemId;
    private String message;

    public static ChecklistItemDeleteResponse success(Long checklistId) {
        return ChecklistItemDeleteResponse.builder()
                .checklistItemId(checklistId)
                .message("체크리스트 삭제 완료")
                .build();
    }
}