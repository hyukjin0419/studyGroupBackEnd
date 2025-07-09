package com.studygroup.studygroupbackend.security.controller;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenRequest;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenResponse;
import com.studygroup.studygroupbackend.security.service.AuthService;;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Tag(name = "Auth", description = "로그인/회원가입 관련 API")
@Slf4j
//@RestController
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Operation(summary = "회원가입 API")
    @PostMapping("/create_member")
    public ResponseEntity<MemberCreateResponse> createMember(@RequestBody MemberCreateRequest request) {
        log.info(request.getEmail());
        return ResponseEntity.ok(authService.createMember(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue/access_token")
    public ResponseEntity<RefreshTokenResponse> reissueAccessToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");
        authService.logout(accessToken, Long.valueOf(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }
}
