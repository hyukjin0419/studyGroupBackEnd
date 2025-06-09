package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.ChecklistDto;
import com.studygroup.studygroupbackend.entity.ChecklistItem;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.ChecklistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistServiceImpl implements ChecklistService {
    private final ChecklistItemRepository checklistItemRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @Override
    public ChecklistDto.CreateResDto createChecklist(Long createdId, ChecklistDto.CreateReqDto request) {
        Member creator = memberRepository.findById(createdId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Study study = null;
        if (request.getStudyId() != null) {
            study = studyRepository.findById(request.getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다"));
        }

        ChecklistItem checklistItem = ChecklistItem.of(study, creator, request.getContent(), request.getDueDate());
        checklistItemRepository.save(checklistItem);
        return ChecklistDto.CreateResDto.fromEntity(checklistItem);
    }

    @Override
    public void updateContent(Long checklistId, String content) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));
        checklistItem.updateContent(content);
    }

    @Override
    public void updateDueDate(Long checklistId, java.time.LocalDateTime dueDate) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));

        checklistItem.updateDueDate(dueDate);
    }

    @Override
    @Transactional(readOnly = true)
    public ChecklistDto.DetailResDto getChecklistDetail(Long checklistId) {
        ChecklistItem checklistItem = checklistItemRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));

        return ChecklistDto.DetailResDto.fromEntity(checklistItem);
    }

    @Override
    public ChecklistDto.DeleteResDto deleteChecklist(Long checklistId) {
        checklistItemRepository.deleteById(checklistId);
        return ChecklistDto.DeleteResDto.success(checklistId);
    }
}
