package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.*;
import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemContentUpdateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemReorderRequest;
import com.studygroup.studygroupbackend.repository.*;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistItemServiceImpl implements ChecklistItemService {
    private static final Logger log = LoggerFactory.getLogger(ChecklistItemServiceImpl.class);
    private final ChecklistItemRepository checklistItemRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    public ChecklistItemDetailResponse createChecklistItemOfStudy(Long creatorId, Long studyId, ChecklistItemCreateRequest request){
        Member creator = memberRepository.findByIdAndDeletedFalse(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다"));

        if (request.getType() != ChecklistItemType.STUDY) return null;

        Study study = studyRepository.findByIdAndDeletedFalse(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        StudyMember assignee = studyMemberRepository.findByIdAndDeletedFalse(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("할당 대상을 찾을 수 없습니다"));

        ChecklistItem item = ChecklistItem.ofGroup(
                study,
                creator,
                assignee,
                request.getContent(),
                request.getOrderIndex(),
                request.getTargetDate(),
                null
        );

        checklistItemRepository.save(item);

        return ChecklistItemDetailResponse.fromEntity(item,request.getTempId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistItemDetailResponse> getStudyItemsByDate(Long studyId, LocalDate targetDate){
        return checklistItemRepository.findByStudyIdAndTargetDate(studyId, targetDate).stream()
                .map(ChecklistItemDetailResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistItemDetailResponse> getStudyItemsByWeek(Long studyId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        List<ChecklistItem> items = checklistItemRepository.findByStudyIdAndTargetDateBetween(studyId, startDate, endDate);
        return items.stream()
                .map(ChecklistItemDetailResponse::fromEntity)
                .toList();
    }

    @Override
    public void updateChecklistItemContent(Long checklistItemId, ChecklistItemContentUpdateRequest request) {
        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트 아이템을 찾을 수 없습니다"));

//        log.info("content : {}", request.getContent());
        item.updateContent(request.getContent());
    }

    @Override
    public void updateChecklistItemStatus(Long checklistItemId) {
        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트 아이템을 찾을 수 없습니다"));

        boolean status = item.isCompleted();
        item.updateStatus(!status);
    }

    @Override
    public void reorderChecklistItems(List<ChecklistItemReorderRequest> requestList) {
        for (ChecklistItemReorderRequest req : requestList) {
            ChecklistItem item = checklistItemRepository.findById(req.getChecklistItemId())
                    .orElseThrow(() -> new EntityNotFoundException("체크리스트 항목을 찾을 수 없습니다."));

            StudyMember studyMember = studyMemberRepository.findById(req.getStudyMemberId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디 멤버를 찾을 수 없습니다."));

            item.updateOrderIndex(req.getOrderIndex());
            item.updateStudyMemberId(studyMember);
        }
    }

    @Override
    public void softDeleteChecklistItems(Long checklistItemId) {
        ChecklistItem item = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트 아이템을 찾을 수 없습니다"));

        item.softDeletion();
    }
}
