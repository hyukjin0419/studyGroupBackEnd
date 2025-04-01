package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Checklist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResDto {
        private Long checklistId;

        public static CreateResDto fromEntity(Checklist checklist) {
            return CreateResDto.builder()
                    .checklistId(checklist.getId())
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    //여기에는 왜 createdAt하고 modifiedAt이 있찌?
    public static class DetailResDto {
        private Long id;
        private Long creatorId;
        private Long studyId;
        private String content;
        private LocalDateTime dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static DetailResDto fromEntity(Checklist checklist) {
            return DetailResDto.builder()
                    .id(checklist.getId())
                    .creatorId(checklist.getCreator().getId())
                    .studyId(checklist.getStudy() != null ? checklist.getStudy().getId() : null)
                    .content(checklist.getContent())
                    .dueDate(checklist.getDueDate() != null ? checklist.getDueDate() : null)
                    .createdAt(checklist.getCreateAt())
                    .modifiedAt(checklist.getModifiedAt())
                    .build();
        }
    }

    //삭제 추가
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResDto{
        private Long checklistId;
        private String message;

        public static DeleteResDto success(Long checklistId) {
            return DeleteResDto.builder()
                    .checklistId(checklistId)
                    .message("체크리스트 삭제 완료")
                    .build();
        }
    }
}
