package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.service.PersonalChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonalChecklistServiceImpl implements PersonalChecklistService {

    private final ChecklistItemRepository checklistItemRepository;

    @Override
    public List<ChecklistItemDetailResponse> getPersonalChecklists(Long memberId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return checklistItemRepository.findAllByStudyMember_Member_IdAndTargetDateBetween(memberId, startDate, endDate)
                .stream()
                .map(ChecklistItemDetailResponse::fromEntity)
                .toList();
    }
}
