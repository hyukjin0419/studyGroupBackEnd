package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.entity.ChecklistItem;
import com.studygroup.studygroupbackend.entity.ChecklistItemAssignment;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.repository.ChecklistItemAssignmentRepository;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.ChecklistItemAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistItemAssignmentServiceImpl implements ChecklistItemAssignmentService {

    private final ChecklistItemAssignmentRepository checklistItemAssignmentRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    @Override
    public ChecklistMemberDto.AssignResDto assignChecklistItem(ChecklistMemberDto.AssignReqDto request) {
        ChecklistItem checklistItem = checklistItemRepository.findById(request.getChecklistId())
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다"));
        Study study = studyRepository.findById(request.getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다"));


        int studyOrder = checklistItemAssignmentRepository.findMaxStudyOrderIndex(checklistItem.getStudy().getId()).orElse(0)+1;
        int personalOrder = checklistItemAssignmentRepository.findMaxPersonalOrderIndex(member.getId()).orElse(0)+1;


        ChecklistItemAssignment cm = ChecklistItemAssignment.assign(checklistItem, member, study, studyOrder,personalOrder);
        checklistItemAssignmentRepository.save(cm);
        return ChecklistMemberDto.AssignResDto.fromEntity(cm);
    }

    @Override
    public ChecklistMemberDto.ChangeStatusResDto changeStatusOfChecklistItem(ChecklistMemberDto.ChangeStatusReqDto request) {
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findByChecklistItemIdAndMemberId(
                        request.getChecklistId(), request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));


        cm.changeStatus();

        return ChecklistMemberDto.ChangeStatusResDto.fromEntity(cm);
    }

    @Override
    @Transactional(readOnly = true)//본인 체크리스트 조회용
    public List<ChecklistMemberDto.MemberChecklistResDto> getChecklistItemByMemberId(Long memberId) {
        List<ChecklistItemAssignment> list = checklistItemAssignmentRepository.findAllByMemberIdOrderByPersonalOrderIndexAsc(memberId);
        return list.stream().map(ChecklistMemberDto.MemberChecklistResDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)//스터디별 체크리스트 조회용
    public List<ChecklistMemberDto.StudyChecklistMemberResDto> getChecklistMembersByStudyId(Long studyId) {
        List<ChecklistItemAssignment> list = checklistItemAssignmentRepository.findAllByChecklistItem_Study_IdOrderByStudyOrderIndexAsc(studyId);
        return list.stream().map(ChecklistMemberDto.StudyChecklistMemberResDto::fromEntity).toList();
    }

    @Override
    public ChecklistMemberDto.UnassignResDto unassignChecklistItem(Long checklistId, Long memberId){
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findByChecklistItemIdAndMemberId(
                        checklistId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));

        return ChecklistMemberDto.UnassignResDto.success(checklistId, memberId);
    }

    @Override
    public void updateStudyOrderIndex(Long checklistMemberId, int newIndex) {
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findById(checklistMemberId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistMember not found"));
        cm.updateStudyOrderIndex(newIndex);
    }

    @Override
    public void updatePersonalOrderIndex(Long checklistMemberId, int newIndex) {
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findById(checklistMemberId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistMember not found"));
        cm.updatePersonalOrderIndex(newIndex);
    }
}
