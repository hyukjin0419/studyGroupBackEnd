package com.studygroup.studygroupbackend.controller.admin;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.service.MemberService;
import com.studygroup.studygroupbackend.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class MemberController {
    private final MemberService memberService;
    private final StudyService studyService;

    @Operation(summary = "회원 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @Operation(summary = "회원 업데이트 API")
    @PostMapping("/{id}")
    public ResponseEntity<MemberDetailResponse> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.updateMember(memberId, request));
    }

    @Operation(summary = "회원 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberDeleteResponse> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }

    @Operation(summary = "전체 회원 목록 조회 API")
    @GetMapping
    public ResponseEntity<List<MemberDetailResponse>> getAllMembers(){
        return ResponseEntity.ok(memberService.getAllMembers());
    }

//    @Operation(summary = "회원 아이디로 스터디 목록 조회 API")
//    @GetMapping("/{memberId}/studies")
//    public ResponseEntity<List<StudyListResponse>> getStudiesByMemberId(@PathVariable Long memberId) {
//        return ResponseEntity.ok(studyService.getStudiesByMemberId(memberId));
//    }
}


