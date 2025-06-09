package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.studymember.StudyMemberInviteRequest;
import com.studygroup.studygroupbackend.dto.studymember.StudyMemberInviteResponse;
import com.studygroup.studygroupbackend.dto.studymember.StudyMemberRemoveResponse;

import java.util.List;

public interface StudyMemberService {
    StudyMemberInviteResponse inviteMember(Long studyId, Long leaderId, StudyMemberInviteRequest request);
    StudyMemberRemoveResponse removeMember(Long studyId, Long leaderId, Long memberId);
    List<StudyListResponse> findStudiesByMemberId(Long memberId);
}
