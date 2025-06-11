package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.MemberChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.StudyChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.unassign.ChecklistItemUnassignResponse;
import com.studygroup.studygroupbackend.service.ChecklistItemAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChecklistItemAssignment", description = "체크리스트 할당 관련 API")
@RestController
@RequestMapping("/checklist-item-assignments")
@RequiredArgsConstructor
public class ChecklistItemAssignmentController {

    private final ChecklistItemAssignmentService checklistItemAssignmentService;

    @Operation(summary = "체크리스트 할당 API", description = "생성되어 있는 체크리스트를 특정 멤버에게 할당합니다.")
    @PostMapping("/assign")
    public ResponseEntity<ChecklistItemAssignResponse> assignChecklistItem(
            @RequestBody ChecklistItemAssignRequest request) {
        return ResponseEntity.ok(checklistItemAssignmentService.assignChecklistItem(request));
    }

    @Operation(summary = "체크리스트 체크/언체크 API", description = "체크리스트 체크박스의 값을 변경합니다.")
    @PostMapping("/change-status")
    public ResponseEntity<ChecklistItemChangeStatusResponse> changeStatusOfChecklistItem(
            @RequestBody ChecklistItemChangeStatusRequest request) {
        return ResponseEntity.ok(checklistItemAssignmentService.changeStatusOfChecklistItem(request));
    }

    @Operation(summary = "체크리스트 할당 해제 API", description = "특정 멤버에게 할당 되어 있는 체크리스트를 할당해제 합니다.")
    @DeleteMapping("/{checklistId}/members/{memberId}")
    public ResponseEntity<ChecklistItemUnassignResponse> unassignChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistItemAssignmentService.unassignChecklistItem(checklistId, memberId));
    }

    @Operation(summary = "체크리스트 멤버 아이디로 전체 조회 API (인증 필요 예정)", description = "personalOrderIndex 기준 오름차순 정렬.")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MemberChecklistItemDetailResponse>> getChecklistByMemberId(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistItemAssignmentService.getChecklistItemByMemberId(memberId));
    }

    @Operation(summary = "체크리스트 스터디 아이디로 전체 조회 API", description = "personalOrderIndex 기준 오름차순 정렬.")
    @GetMapping("/study/{studyId}")
    public ResponseEntity<List<StudyChecklistItemDetailResponse>> getChecklistMembersByStudyId(
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(checklistItemAssignmentService.getChecklistMembersByStudyId(studyId));
    }
}
