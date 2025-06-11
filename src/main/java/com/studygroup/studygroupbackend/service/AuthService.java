package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;

public interface AuthService {
    MemberLoginResponse login(MemberLoginRequest request);
}
