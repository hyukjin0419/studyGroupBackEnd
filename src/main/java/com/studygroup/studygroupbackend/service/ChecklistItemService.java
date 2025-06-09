package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.delete.ChecklistItemDeleteResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.detail.ChecklistItemDetailResponse;

public interface ChecklistItemService {
    public ChecklistItemCreateResponse createChecklist(Long createdId, ChecklistItemCreateRequest request);
    public void updateContent(Long checklistId, String content);
    public void updateDueDate(Long checklistId, java.time.LocalDateTime dueDate);
    public ChecklistItemDetailResponse getChecklistDetail(Long checklistId) ;
    public ChecklistItemDeleteResponse deleteChecklist(Long checklistId);
}
