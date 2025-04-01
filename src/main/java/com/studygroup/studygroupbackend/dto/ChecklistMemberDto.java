package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.ChecklistMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChecklistMemberDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignReqDto {
        private Long checklistId;
        private Long memberId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignResDto {
        private Long checklistId;
        private Long memberId;
        private LocalDateTime assignedAt;

        public static AssignResDto fromEntity(ChecklistMember cm) {
            return AssignResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .memberId(cm.getMember().getId())
                    .assignedAt(cm.getAssignedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteReqDto{
        private Long checklistId;
        private Long memberId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteResDto {
        private Long checklistId;
        private Long memberId;
        private boolean isCompleted;
        private LocalDateTime completedAt;

        public static CompleteResDto fromEntity(ChecklistMember cm) {
            return CompleteResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .memberId(cm.getMember().getId())
                    .isCompleted(cm.isCompleted())
                    .completedAt(cm.getCompletedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberChecklistResDto {
        private Long checklistId;
        private String content;
        private boolean isCompleted;
        private LocalDateTime dueDate;
        private LocalDateTime completedAt;
        private LocalDateTime assignedAt;

        public static MemberChecklistResDto fromEntity(ChecklistMember cm) {
            return MemberChecklistResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .content(cm.getChecklist().getContent())
                    .isCompleted(cm.isCompleted())
                    .completedAt(cm.getCompletedAt())
                    .assignedAt(cm.getAssignedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnassignResDto{
        private Long checklistId;
        private Long memberId;
        private String message;

        public static UnassignResDto success(Long checklistId, Long memberId) {
            return UnassignResDto.builder()
                    .checklistId(checklistId)
                    .memberId(memberId)
                    .message("체크리스트 할당 해제 완료")
                    .build();
        }
    }

}
