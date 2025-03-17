package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.StudyMemberDto;

public interface StudyMemberService {
    StudyMemberDto.InviteResDto inviteMember(Long studyId, Long leaderId, StudyMemberDto.InviteReqDto request);
    StudyMemberDto.RemoveResDto removeMember(Long studyId, Long leaderId, Long memberId);
}
