package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberEmailUpdateRequest;
import com.studygroup.studygroupbackend.dto.member.update.MemberDisplayNameUpdateRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Me", description = "사용자 본인 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {
    private final MemberService memberService;

    @Operation(summary = "[USER] 본인 조회 API")
    @GetMapping
    public ResponseEntity<MemberDetailResponse> getMyInfo(@CurrentUser CustomUserDetails userDetails){
        log.info("working");
        return ResponseEntity.ok(memberService.getMemberById(userDetails.getMemberId()));
    }

    @Operation(summary = "[USER] 본인 UserName update API")
    @PostMapping("/update-user-name")
    public ResponseEntity<MemberDetailResponse> updateMyDisplayName(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody MemberDisplayNameUpdateRequest request
    ) {
        return ResponseEntity.ok(memberService.updateMyDisplayName(userDetails.getMemberId(), request));
    }

    @Operation(summary = "[USER] 본인 Email update API")
    @PostMapping("/update-email")
    public ResponseEntity<MemberDetailResponse> updateMyEmail(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody MemberEmailUpdateRequest request
    ) {
        return ResponseEntity.ok(memberService.updateMemberEmail(userDetails.getMemberId(), request));
    }

    @Operation(summary = "[USER] 본인 회원탈퇴 API")
    @DeleteMapping
    public ResponseEntity<MemberDeleteResponse> deleteMyAccount(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(memberService.deleteMember(userDetails.getMemberId()));
    }
}
