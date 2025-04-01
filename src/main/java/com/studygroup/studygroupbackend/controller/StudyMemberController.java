package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.StudyMemberDto;
import com.studygroup.studygroupbackend.service.StudyMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "StudyMember", description = "스터디 멤버에 관한 API")
@RestController
@RequestMapping("/studies/{studyId}/members")
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    @Operation(summary = "스터디에 멤버 초청 API", description = "스터디에 멤버를 초청합니다.")
    @PostMapping
    public ResponseEntity<StudyMemberDto.InviteResDto> inviteMember(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader-Id") Long leaderId,
            @RequestBody StudyMemberDto.InviteReqDto request) {
        return ResponseEntity.ok(studyMemberService.inviteMember(studyId, leaderId, request));
    }

    @Operation(summary = "스터디에서 멤버 삭제 API", description = "스터디에서 멤버를 추방합니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<StudyMemberDto.RemoveResDto> removeMember(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader-Id") Long leaderId,
            @PathVariable Long memberId){
        return ResponseEntity.ok(studyMemberService.removeMember(studyId, leaderId, memberId));
    }
}
