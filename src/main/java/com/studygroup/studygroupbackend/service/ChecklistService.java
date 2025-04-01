package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.ChecklistDto;

public interface ChecklistService {
    public ChecklistDto.CreateResDto createChecklist(Long createdId, ChecklistDto.CreateReqDto request);
    public void updateContent(Long checklistId, String content);
    public void updateDueDate(Long checklistId, java.time.LocalDateTime dueDate);
    public ChecklistDto.DetailResDto getChecklistDetail(Long checklistId) ;
    public ChecklistDto.DeleteResDto deleteChecklist(Long checklistId);
}
