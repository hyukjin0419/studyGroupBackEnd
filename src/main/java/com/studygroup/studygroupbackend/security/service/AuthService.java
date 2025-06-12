package com.studygroup.studygroupbackend.security.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;

public interface AuthService {
    MemberCreateResponse createMember(MemberCreateRequest request);
    MemberLoginResponse login(MemberLoginRequest request);
    RefreshTokenResponse reissueAccessToken(String refreshToken);
    void logout(Long memberId);
}
