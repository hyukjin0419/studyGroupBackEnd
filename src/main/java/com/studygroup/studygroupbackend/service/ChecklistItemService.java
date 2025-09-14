package com.studygroup.studygroupbackend.service;


import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemContentUpdateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemReorderRequest;

import java.time.LocalDate;
import java.util.List;

public interface ChecklistItemService {
    ChecklistItemDetailResponse createChecklistItemOfStudy(Long creatorId, Long studyId, ChecklistItemCreateRequest request);
    List<ChecklistItemDetailResponse> getStudyItemsByDate(Long studyId, LocalDate targetDate);
    void updateChecklistItemContent(Long checklistItemId, ChecklistItemContentUpdateRequest request);
    void updateChecklistItemStatus(Long checklistItemId);
    void reorderChecklistItems(List<ChecklistItemReorderRequest> requestList);
    void softDeleteChecklistItems(Long checklistItemId);
}
