package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.service.ChecklistMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklist-members")
@RequiredArgsConstructor
public class ChecklistMemberController {

    private final ChecklistMemberService checklistMemberService;

    @PostMapping("/assign")
    public ResponseEntity<ChecklistMemberDto.AssignResDto> assignChecklistToMember(
            @RequestBody ChecklistMemberDto.AssignReqDto request) {
        return ResponseEntity.ok(checklistMemberService.assignChecklist(request));
    }

    @PatchMapping("/change-status")
    public ResponseEntity<ChecklistMemberDto.ChangeStatusResDto> changeStatus(
            @RequestBody ChecklistMemberDto.ChangeStatusReqDto request) {
        return ResponseEntity.ok(checklistMemberService.changeStatus(request));
    }

    @DeleteMapping("/{checklistId}/members/{memberId}")
    public ResponseEntity<ChecklistMemberDto.UnassignResDto> unassignChecklist(
            @PathVariable Long checklistId,
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.unassignChecklist(checklistId, memberId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ChecklistMemberDto.MemberChecklistResDto>> getChecklistMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(checklistMemberService.getMemberChecklists(memberId));
    }

    @GetMapping("/study/{studyId}")
    public ResponseEntity<List<ChecklistMemberDto.StudyChecklistMemberResDto>> getChecklistByStudy(
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(checklistMemberService.getChecklistMembersByStudyId(studyId));
    }
}
