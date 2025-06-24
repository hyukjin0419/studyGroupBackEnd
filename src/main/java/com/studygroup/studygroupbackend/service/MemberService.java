package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberUpdateRequest;

import java.util.List;

public interface MemberService {
    MemberDetailResponse getMemberById(Long id);
    MemberDetailResponse updateMember(MemberUpdateRequest request);
    MemberDeleteResponse deleteMember(Long id);
    List<MemberDetailResponse> getAllMembers();
}
