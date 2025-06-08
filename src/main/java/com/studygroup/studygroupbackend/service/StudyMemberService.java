package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.StudyMemberDto;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;

import java.util.List;

public interface StudyMemberService {
    StudyMemberDto.InviteResDto inviteMember(Long studyId, Long leaderId, StudyMemberDto.InviteReqDto request);
    StudyMemberDto.RemoveResDto removeMember(Long studyId, Long leaderId, Long memberId);
    List<StudyListResponse> findStudiesByMemberId(Long memberId);
}
