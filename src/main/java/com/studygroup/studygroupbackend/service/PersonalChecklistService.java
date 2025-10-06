package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.personalChecklist.PersonalChecklistDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface PersonalChecklistService {
    List<PersonalChecklistDetailResponse> getPersonalChecklists(Long memberId, LocalDate date);
}
