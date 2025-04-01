package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.MemberDto;
import com.studygroup.studygroupbackend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입 API")
    @PostMapping//signin으로 바꾸고 회원 목록 조회를 기본 으로 매핑해야하나? 회원 목록 조회가 필요한가? admin에서는 maybe.
    //일단 회원 목록 조회는 Getmapping 때문에 해당 메소드를 바꿀 이유는 없다. 다만 clear할 수 있도록 signin으로 바꾸는 건 좋은 수 있다.

    public ResponseEntity<MemberDto.CreateResDto> createMember(@RequestBody MemberDto.CreateReqDto request) {
        return ResponseEntity.ok(memberService.creatMember(request));
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<MemberDto.LoginResDto> login(@RequestBody MemberDto.LoginReqDto request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @Operation(summary = "회원 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.DetailResDto> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @Operation(summary = "회원 업데이트 API")
    @PutMapping("/{id}")//Put 아니라 Patch 써야하는 거 아닌가? 맞다! patch 업데이트 바람.-> patch를 못쓰는 서버도 있다고? 그러면 post 써라!
    public ResponseEntity<MemberDto.UpdateResDto> updateMember(@RequestBody MemberDto.UpdateReqDto request) {
        return ResponseEntity.ok(memberService.updateMember(request));
    }

    @Operation(summary = "회원 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<MemberDto.DeleteResDto> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }
}
