package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;

import java.util.List;

public interface ChecklistMemberService {
    public ChecklistMemberDto.AssignResDto assignChecklist(ChecklistMemberDto.AssignReqDto request);
    public ChecklistMemberDto.CompleteResDto completeReqDto(ChecklistMemberDto.CompleteReqDto request);
    public List<ChecklistMemberDto.MemberChecklistResDto> getMemberChecklists(Long memberId);
    public List<ChecklistMemberDto.StudyChecklistMemberResDto> getChecklistMembersByStudyId(Long studyId);
}
