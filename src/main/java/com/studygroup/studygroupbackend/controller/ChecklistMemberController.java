package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.service.ChecklistMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklist-members")
@RequiredArgsConstructor
public class ChecklistMemberController {

    private final ChecklistMemberService checklistMemberService;

    @Operation(summary = "체크리스트 할당 API")
    @PostMapping("/assign")
    public ResponseEntity<ChecklistMemberDto.AssignResDto> assignChecklistToMember(
            @RequestBody ChecklistMemberDto.AssignReqDto request) {
        return ResponseEntity.ok(checklistMemberService.assignChecklist(request));
    }

    @Operation(summary = "체크리스트 체크/언체크 API")
    @PatchMapping("/change-status")
    public ResponseEntity<ChecklistMemberDto.ChangeStatusResDto> changeStatus(
            @RequestBody ChecklistMemberDto.ChangeStatusReqDto request) {
        return ResponseEntity.ok(checklistMemberService.changeStatus(request));
    }

    @Operation(summary = "체크리스트 할당 해제 API")
    @DeleteMapping("/{checklistId}/members/{memberId}")
    public ResponseEntity<ChecklistMemberDto.UnassignResDto> unassignChecklist(
            @PathVariable Long checklistId,
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.unassignChecklist(checklistId, memberId));
    }

    @Operation(summary = "체크리스트 멤버 아이디로 전체 조회 API")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ChecklistMemberDto.MemberChecklistResDto>> getChecklistMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.getMemberChecklists(memberId));
    }

    @Operation(summary = "체크리스트 스터디 아이디로 전체 조회 API")
    @GetMapping("/study/{studyId}")
    public ResponseEntity<List<ChecklistMemberDto.StudyChecklistMemberResDto>> getChecklistByStudy(
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(checklistMemberService.getChecklistMembersByStudyId(studyId));
    }
}
