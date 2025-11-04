package com.studygroup.studygroupbackend.security.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenResponse;
import com.studygroup.studygroupbackend.dto.member.create.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.create.MemberCreateResponse;

public interface AuthService {
    MemberCreateResponse createMember(MemberCreateRequest request);
    MemberLoginResponse login(MemberLoginRequest request);
    RefreshTokenResponse reissueAccessToken(String refreshToken);
//    RefreshTokenResponse reissueRefreshToken(String refreshToken);
    void logout(String accessToken, Long memberId, String deviceToken);
}
