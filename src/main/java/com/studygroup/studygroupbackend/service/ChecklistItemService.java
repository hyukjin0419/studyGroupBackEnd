package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.delete.ChecklistItemDeleteResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.detail.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.update.ChecklistItemUpdateRequest;

public interface ChecklistItemService {
    public ChecklistItemCreateResponse createChecklistItem(Long createdId, ChecklistItemCreateRequest request);
    public void updateChecklistItem(Long id, ChecklistItemUpdateRequest request);
    public ChecklistItemDetailResponse getChecklistItemDetail(Long checklistId) ;
    public ChecklistItemDeleteResponse deleteChecklistItem(Long checklistId);
}
