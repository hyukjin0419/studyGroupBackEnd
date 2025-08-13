package com.studygroup.studygroupbackend.service;


import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChecklistItemService {
    void createChecklistItemOfStudy(Long creatorId, Long studyId, ChecklistItemCreateRequest request);
    List<ChecklistItemDetailResponse> getStudyItemsByDate(Long studyId, LocalDate targetDate);
//    public void updateChecklistItem(Long id, ChecklistItemUpdateRequest request);
//    public ChecklistItemDetailResponse getChecklistItemDetail(Long checklistId) ;
//    public ChecklistItemDeleteResponse deleteChecklistItem(Long checklistId);
}
