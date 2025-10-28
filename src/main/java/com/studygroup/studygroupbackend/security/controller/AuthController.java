package com.studygroup.studygroupbackend.security.controller;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.login.MemberLogoutRequest;
import com.studygroup.studygroupbackend.dto.member.create.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.create.MemberCreateResponse;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenRequest;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenResponse;
import com.studygroup.studygroupbackend.security.service.AuthService;;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "로그인/회원가입 관련 API")
@Slf4j
@RestController
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
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody MemberLogoutRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        log.info("Lougout: " + authorizationHeader);

        String accessToken = authorizationHeader.replace("Bearer ", "");
        authService.logout(accessToken, userDetails.getMemberId(), request.getDeviceToken());
        return ResponseEntity.ok().build();
    }
}
