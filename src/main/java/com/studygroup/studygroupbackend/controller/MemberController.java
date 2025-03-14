package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.MemberDto;
import com.studygroup.studygroupbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDto.CreateResDto> createMember(@RequestBody MemberDto.CreateReqDto request) {
        return ResponseEntity.ok(memberService.creatMember(request));
    }

    @PostMapping
    public ResponseEntity<MemberDto.LoginResDto> login(@RequestBody MemberDto.LoginReqDto request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.DetailResDto> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto.UpdateResDto> updateMember(@RequestBody MemberDto.UpdateReqDto request) {
        return ResponseEntity.ok(memberService.updateMember(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberDto.DeleteResDto> deleteMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.deleteMember(id));
    }
}
