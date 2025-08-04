package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.service.ChecklistItemAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChecklistItemAssignment", description = "체크리스트 할당 관련 API")
@RestController
@RequestMapping("/checklist-item-assignments")
@RequiredArgsConstructor
public class ChecklistItemAssignmentController {

    private final ChecklistItemAssignmentService checklistItemAssignmentService;

//    @Operation(summary = "본인이 리더로써 스터디 생성 API", description = "[USER] 새로운 스터디를 리더로써 등록합니다.")
//    @PostMapping("/create")
//    public ResponseEntity<StudyCreateResponse> createStudy(
//            @CurrentUser CustomUserDetails userDetails,
//            @RequestBody StudyCreateRequest request) {
//        return ResponseEntity.ok(studyService.createStudy(userDetails.getMemberId(),request));
//    }
}
