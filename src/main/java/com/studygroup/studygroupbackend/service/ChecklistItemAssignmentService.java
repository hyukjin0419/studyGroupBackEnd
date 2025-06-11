package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.MemberChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.StudyChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.unassign.ChecklistItemUnassignResponse;

import java.util.List;

public interface ChecklistItemAssignmentService {
    public ChecklistItemAssignResponse assignChecklistItem(ChecklistItemAssignRequest request);
    public ChecklistItemChangeStatusResponse changeStatusOfChecklistItem(ChecklistItemChangeStatusRequest request);
    public List<MemberChecklistItemDetailResponse> getChecklistItemByMemberId(Long memberId);
    public List<StudyChecklistItemDetailResponse> getChecklistMembersByStudyId(Long studyId);
    public ChecklistItemUnassignResponse unassignChecklistItem(Long checklistId, Long memberId);
    public void updateStudyOrderIndex(Long checklistMemberId, int newIndex);
    public void updatePersonalOrderIndex(Long checklistMemberId, int newIndex);
}
