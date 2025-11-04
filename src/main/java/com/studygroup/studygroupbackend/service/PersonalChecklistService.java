package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface PersonalChecklistService {
    List<ChecklistItemDetailResponse> getPersonalChecklists(Long memberId, LocalDate date);
}
