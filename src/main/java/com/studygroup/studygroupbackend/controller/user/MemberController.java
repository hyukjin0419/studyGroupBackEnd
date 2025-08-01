package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.member.search.MemberSearchResponse;
import com.studygroup.studygroupbackend.service.MemberService;
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
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "실시간 회원 검색", description = "사용자 이름 일부로 멤버를 검색합니다.")
    @GetMapping("{studyId}/search")
    public ResponseEntity<List<MemberSearchResponse>> searchMembers(@PathVariable Long studyId, @RequestParam String keyword){
        return ResponseEntity.ok(memberService.searchAvailableMembersByUserName(keyword, studyId));
    }
}


