package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.*;
import com.studygroup.studygroupbackend.domain.status.ChecklistItemType;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.repository.*;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistItemServiceImpl implements ChecklistItemService {
    private final ChecklistItemRepository checklistItemRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    public void createChecklistItemOfStudy(Long creatorId, Long studyId, ChecklistItemCreateRequest request){
        Member creator = memberRepository.findByIdAndDeletedFalse(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("해당 멤버를 찾을 수 없습니다"));

        if (request.getType() != ChecklistItemType.STUDY) return;

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
    }

    @Override
    public List<ChecklistItemDetailResponse> getStudyItemsByDate(Long studyId, LocalDate targetDate){
        return checklistItemRepository.findByStudyIdAndTargetDate(studyId, targetDate).stream()
                .map(ChecklistItemDetailResponse::fromEntity)
                .toList();
    }
}



//
//    @Override
//    public ChecklistItemCreateResponse createChecklistItemOfStudy(Long createdId, ChecklistItemCreateRequest request) {
//        Member creator = memberRepository.findById(createdId)
//                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
//
//        Study study = null;
//        if (request.getStudyId() != null) {
//            study = studyRepository.findById(request.getStudyId())
//                    .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다"));
//        }
//
//        ChecklistItem checklistItem = ChecklistItem.ofGroup(study, creator, request.getContent(), request.getDueDate());
//        checklistItemRepository.save(checklistItem);
//        return ChecklistItemCreateResponse.fromEntity(checklistItem);
//    }
//
//    @Override
//    public void updateChecklistItem(Long id, ChecklistItemUpdateRequest request) {
//        ChecklistItem item = checklistItemRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("체크리스트 항목이 존재하지 않습니다."));
//
//        if (request.getContent() != null) {
//            item.updateContent(request.getContent());
//        }
//
//        if (request.getDueDate() != null) {
//            item.updateDueDate(request.getDueDate());
//        }
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public ChecklistItemDetailResponse getChecklistItemDetail(Long checklistId) {
//        ChecklistItem checklistItem = checklistItemRepository.findById(checklistId)
//                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));
//
//        return ChecklistItemDetailResponse.fromEntity(checklistItem);
//    }
//
//    @Override
//    public ChecklistItemDeleteResponse deleteChecklistItem(Long checklistId) {
//        checklistItemRepository.deleteById(checklistId);
//        return ChecklistItemDeleteResponse.success(checklistId);
//    }
//}
