package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.ChecklistMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class ChecklistMemberDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignReqDto {
        private Long checklistId;
        private Long memberId;
        private Long studyId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignResDto extends BaseResDto{
        private Long checklistId;
        private Long memberId;
        private LocalDateTime assignedAt;

        public static AssignResDto fromEntity(ChecklistMember cm) {
            return AssignResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .memberId(cm.getMember().getId())
                    .assignedAt(cm.getAssignedAt())
                    .createdAt(cm.getCreatedAt())
                    .modifiedAt(cm.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeStatusReqDto{
        private Long checklistId;
        private Long memberId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeStatusResDto extends BaseResDto{
        private Long checklistId;
        private Long memberId;
        private boolean isCompleted;
        private LocalDateTime completedAt;

        public static ChangeStatusResDto fromEntity(ChecklistMember cm) {
            return ChangeStatusResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .memberId(cm.getMember().getId())
                    .isCompleted(cm.isCompleted())
                    .completedAt(cm.getCompletedAt())
                    .createdAt(cm.getCompletedAt())
                    .modifiedAt(cm.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    //본인 체크리스트 조회용
    public static class MemberChecklistResDto extends BaseResDto{
        private Long checklistId;
        private String content;
        private boolean isCompleted;
        private LocalDateTime dueDate;
        private LocalDateTime completedAt;
        private LocalDateTime assignedAt;
        private Integer studyOrderIndex;
        private Integer personalOrderIndex;

        public static MemberChecklistResDto fromEntity(ChecklistMember cm) {
            return MemberChecklistResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .content(cm.getChecklist().getContent())
                    .isCompleted(cm.isCompleted())
                    .dueDate(cm.getChecklist().getDueDate())
                    .completedAt(cm.getCompletedAt())
                    .assignedAt(cm.getAssignedAt())
                    .studyOrderIndex(cm.getStudyOrderIndex())
                    .personalOrderIndex(cm.getPersonalOrderIndex())
                    .createdAt(cm.getCompletedAt())
                    .modifiedAt(cm.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyChecklistMemberResDto extends BaseResDto{
        private Long checklistId;
        private String content;
        private Long memberId;
        private String memberName;
        private boolean isCompleted;
        private LocalDateTime dueDate;
        private LocalDateTime completedAt;
        private LocalDateTime assignedAt;
        private Integer studyOrderIndex;
        private Integer personalOrderIndex;

        public static StudyChecklistMemberResDto fromEntity(ChecklistMember cm) {
            return StudyChecklistMemberResDto.builder()
                    .checklistId(cm.getChecklist().getId())
                    .content(cm.getChecklist().getContent())
                    .memberId(cm.getMember().getId())
                    .memberName(cm.getMember().getUserName())
                    .isCompleted(cm.isCompleted())
                    .dueDate(cm.getChecklist().getDueDate())
                    .completedAt(cm.getCompletedAt())
                    .assignedAt(cm.getAssignedAt())
                    .studyOrderIndex(cm.getStudyOrderIndex())
                    .personalOrderIndex(cm.getPersonalOrderIndex())
                    .createdAt(cm.getCompletedAt())
                    .modifiedAt(cm.getModifiedAt())
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
