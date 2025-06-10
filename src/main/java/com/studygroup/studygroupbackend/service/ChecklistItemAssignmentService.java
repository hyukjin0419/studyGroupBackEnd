package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;

import java.util.List;

public interface ChecklistItemAssignmentService {
    public ChecklistMemberDto.AssignResDto assignChecklistItem(ChecklistMemberDto.AssignReqDto request);
    public ChecklistMemberDto.ChangeStatusResDto changeStatusOfChecklistItem(ChecklistMemberDto.ChangeStatusReqDto request);
    public List<ChecklistMemberDto.MemberChecklistResDto> getChecklistItemByMemberId(Long memberId);
    public List<ChecklistMemberDto.StudyChecklistMemberResDto> getChecklistMembersByStudyId(Long studyId);
    public ChecklistMemberDto.UnassignResDto unassignChecklistItem(Long checklistId, Long memberId);
    public void updateStudyOrderIndex(Long checklistMemberId, int newIndex);
    public void updatePersonalOrderIndex(Long checklistMemberId, int newIndex);
}
