package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.ChecklistDto;
import com.studygroup.studygroupbackend.entity.Checklist;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.repository.ChecklistRepository;
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
    private final ChecklistRepository checklistRepository;
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

        Checklist checklist = Checklist.of(study, creator, request.getContent(), request.getDueDate());
        checklistRepository.save(checklist);
        return ChecklistDto.CreateResDto.fromEntity(checklist);
    }

    @Override
    public void updateContent(Long checklistId, String content) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));
        checklist.updateContent(content);
    }

    @Override
    public void updateDueDate(Long checklistId, java.time.LocalDateTime dueDate) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));

        checklist.updateDueDate(dueDate);
    }

    @Override
    @Transactional(readOnly = true)
    public ChecklistDto.DetailResDto getChecklistDetail(Long checklistId) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다."));

        return ChecklistDto.DetailResDto.fromEntity(checklist);
    }

    @Override
    public ChecklistDto.DeleteResDto deleteResDto(Long checklistId) {
        checklistRepository.deleteById(checklistId);
        return ChecklistDto.DeleteResDto.success(checklistId);
    }
}
