package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.search.MemberSearchResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberEmailUpdateRequest;
import com.studygroup.studygroupbackend.dto.member.update.MemberDisplayNameUpdateRequest;

import java.util.List;

public interface MemberService {
    MemberDetailResponse getMemberById(Long memberId);
    MemberDetailResponse updateMyDisplayName(Long memberId, MemberDisplayNameUpdateRequest request);
    MemberDetailResponse updateMemberEmail(Long memberId, MemberEmailUpdateRequest request);
    MemberDeleteResponse deleteMember(Long memberId);
    List<MemberDetailResponse> getAllMembers();
    List<MemberSearchResponse> searchAvailableMembersByUserName(String keyword, Long studyId);
}
