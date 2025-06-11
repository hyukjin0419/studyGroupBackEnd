package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.delete.ChecklistItemDeleteResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.detail.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.update.ChecklistItemUpdateRequest;
import com.studygroup.studygroupbackend.entity.ChecklistItem;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistItemServiceImpl implements ChecklistItemService {
    private final ChecklistItemRepository checklistItemRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @Override
    public ChecklistItemCreateResponse createChecklistItem(Long createdId, ChecklistItemCreateRequest request) {
        Member creator = memberRepository.findById(createdId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Study study = null;
        if (request.getStudyId() != null) {
            study = studyRepository.findById(request.getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다"));
        }

        ChecklistItem checklistItem = ChecklistItem.of(study, creator, request.getContent(), request.getDueDate());
        checklistItemRepository.save(checklistItem);
        return ChecklistItemCreateResponse.fromEntity(checklistItem);
    }

    @Override
    public void updateChecklistItem(Long id, ChecklistItemUpdateRequest request) {
        ChecklistItem item = checklistItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트 항목이 존재하지 않습니다."));

        if (request.getContent() != null) {
            item.updateContent(request.getContent());
        }

        if (request.getDueDate() != null) {
            item.updateDueDate(request.getDueDate());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ChecklistItemDetailResponse getChecklistItemDetail(Long checklistId) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));

        return ChecklistItemDetailResponse.fromEntity(checklistItem);
    }

    @Override
    public ChecklistItemDeleteResponse deleteChecklistItem(Long checklistId) {
        checklistItemRepository.deleteById(checklistId);
        return ChecklistItemDeleteResponse.success(checklistId);
    }
}
