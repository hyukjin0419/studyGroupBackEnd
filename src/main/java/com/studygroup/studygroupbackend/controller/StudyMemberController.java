package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.StudyMemberDto;
import com.studygroup.studygroupbackend.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studies/{studyId}/members")
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    @PostMapping
    public ResponseEntity<StudyMemberDto.InviteResDto> inviteMember(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader-Id") Long leaderId,
            @RequestBody StudyMemberDto.InviteReqDto request) {
        return ResponseEntity.ok(studyMemberService.inviteMember(studyId, leaderId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<StudyMemberDto.RemoveResDto> removeMember(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader-Id") Long leaderId,
            @PathVariable Long memberId){
        return ResponseEntity.ok(studyMemberService.removeMember(studyId, leaderId, memberId));
    }
}
