package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.service.ChecklistMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ChecklistMember", description = "체크리스트 할당 관련 API")
@RestController
@RequestMapping("/checklist-members")
@RequiredArgsConstructor
public class ChecklistMemberController {

    private final ChecklistMemberService checklistMemberService;

    @Operation(summary = "체크리스트 할당 API", description = "생성되어 있는 체크리스트를 특정 멤버에게 할당합니다.")
    @PostMapping("/assign")
    public ResponseEntity<ChecklistMemberDto.AssignResDto> assignChecklistToMember(
            @RequestBody ChecklistMemberDto.AssignReqDto request) {
        return ResponseEntity.ok(checklistMemberService.assignChecklist(request));
    }

    @Operation(summary = "체크리스트 체크/언체크 API", description = "체크리스트 체크박스의 값을 변경합니다.")
    @PatchMapping("/change-status")
    public ResponseEntity<ChecklistMemberDto.ChangeStatusResDto> changeStatus(
            @RequestBody ChecklistMemberDto.ChangeStatusReqDto request) {
        return ResponseEntity.ok(checklistMemberService.changeStatus(request));
    }

    @Operation(summary = "체크리스트 할당 해제 API", description = "특정 멤버에게 할당 되어 있는 체크리스트를 할당해제 합니다.")
    @DeleteMapping("/{checklistId}/members/{memberId}")
    public ResponseEntity<ChecklistMemberDto.UnassignResDto> unassignChecklist(
            @PathVariable Long checklistId,
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.unassignChecklist(checklistId, memberId));
    }

    @Operation(summary = "체크리스트 멤버 아이디로 전체 조회 API (인증 필요 예정)", description = "personalOrderIndex 기준 오름차순 정렬.")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ChecklistMemberDto.MemberChecklistResDto>> getChecklistByMemberId(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.getMemberChecklists(memberId));
    }

    @Operation(summary = "체크리스트 스터디 아이디로 전체 조회 API", description = "personalOrderIndex 기준 오름차순 정렬.")
    @GetMapping("/study/{studyId}")
    public ResponseEntity<List<ChecklistMemberDto.StudyChecklistMemberResDto>> getChecklistByStudyId(
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(checklistMemberService.getChecklistMembersByStudyId(studyId));
    }
}
