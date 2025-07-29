package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.search.MemberSearchResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberUpdateRequest;

import java.util.List;

public interface MemberService {
    MemberDetailResponse getMemberById(Long memberId);
    MemberDetailResponse updateMember(Long memberId, MemberUpdateRequest request);
    MemberDeleteResponse deleteMember(Long memberId);
    List<MemberDetailResponse> getAllMembers();
    List<MemberSearchResponse> searchMembersByUserName(String keyword, Long studyId);
}
