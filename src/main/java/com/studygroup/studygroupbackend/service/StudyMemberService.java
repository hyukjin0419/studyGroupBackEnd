package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.StudyDto;
import com.studygroup.studygroupbackend.dto.StudyMemberDto;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.StudyMember;

import java.util.List;

public interface StudyMemberService {
    StudyMemberDto.InviteResDto inviteMember(Long studyId, Long leaderId, StudyMemberDto.InviteReqDto request);
    StudyMemberDto.RemoveResDto removeMember(Long studyId, Long leaderId, Long memberId);
    List<StudyDto.ListResDto> findStudiesByMemberId(Long memberId);
}
