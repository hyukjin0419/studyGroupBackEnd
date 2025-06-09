package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class ChecklistDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReqDto {
        private Long studyId; //null -> 개인용
        private String content;
        private LocalDateTime dueDate; //null이 기본값
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResDto extends BaseResDto{
        private Long checklistItemId;

        public static CreateResDto fromEntity(ChecklistItem checklistItem) {
            return CreateResDto.builder()
                    .checklistItemId(checklistItem.getId())
                    .createdAt(checklistItem.getCreatedAt())
                    .modifiedAt(checklistItem.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateContentReqDto {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDueDateReqDto {
        private LocalDateTime dueDate;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResDto extends BaseResDto{
        private Long id;
        private Long creatorId;
        private Long studyId; //null 이면 개인용
        private String content;
        private LocalDateTime dueDate;

        public static DetailResDto fromEntity(ChecklistItem checklistItem) {
            return DetailResDto.builder()
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

    //삭제 추가
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResDto{
        private Long checklistItemId;
        private String message;

        public static DeleteResDto success(Long checklistId) {
            return DeleteResDto.builder()
                    .checklistItemId(checklistId)
                    .message("체크리스트 삭제 완료")
                    .build();
        }
    }
}
