//package com.studygroup.studygroupbackend.dto.personalChecklist;
//
//import com.studygroup.studygroupbackend.domain.ChecklistItem;
//import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class PersonalChecklistDetailResponse {
//    private Long id;
//    private ChecklistItemType type;
//    private Long studyId;
//    private Long studyMemberId;
//    private String studyName;
//    private String content;
//    private boolean completed;
//    private LocalDate targetDate;
//    private Integer orderIndex;
//
//    public static PersonalChecklistDetailResponse fromEntity(ChecklistItem item){
//        return PersonalChecklistDetailResponse.builder()
//                .id(item.getId())
//                .type(item.getType())
//                .studyId(item.getStudy().getId())
//                .studyName(item.getStudy().getName())
//                .studyMemberId(item.getStudyMember().getId())
//                .content(item.getContent())
//                .completed(item.isCompleted())
//                .targetDate(item.getTargetDate())
//                .orderIndex(item.getOrderIndex())
//                .build();
//    }
//}
