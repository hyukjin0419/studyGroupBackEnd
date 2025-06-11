package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.jwt.dto.RefreshTokenResponse;

public interface AuthService {
    MemberLoginResponse login(MemberLoginRequest request);
    RefreshTokenResponse reissueAccessToken(String refreshToken);
    public void logout(Long memberId);
}
