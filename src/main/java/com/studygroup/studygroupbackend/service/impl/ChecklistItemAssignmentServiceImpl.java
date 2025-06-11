package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.assign.ChecklistItemAssignResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusRequest;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.changestatus.ChecklistItemChangeStatusResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.MemberChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.detail.StudyChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItemAssignment.unassign.ChecklistItemUnassignResponse;
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
    public ChecklistItemAssignResponse assignChecklistItem(ChecklistItemAssignRequest request) {
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
        return ChecklistItemAssignResponse.fromEntity(cm);
    }

    @Override
    public ChecklistItemChangeStatusResponse changeStatusOfChecklistItem(ChecklistItemChangeStatusRequest request) {
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findByChecklistItemIdAndMemberId(
                        request.getChecklistId(), request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));


        cm.changeStatus();

        return ChecklistItemChangeStatusResponse.fromEntity(cm);
    }

    @Override
    @Transactional(readOnly = true)//본인 체크리스트 조회용
    public List<MemberChecklistItemDetailResponse> getChecklistItemByMemberId(Long memberId) {
        List<ChecklistItemAssignment> list = checklistItemAssignmentRepository.findAllByMemberIdOrderByPersonalOrderIndexAsc(memberId);
        return list.stream().map(MemberChecklistItemDetailResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)//스터디별 체크리스트 조회용
    public List<StudyChecklistItemDetailResponse> getChecklistMembersByStudyId(Long studyId) {
        List<ChecklistItemAssignment> list = checklistItemAssignmentRepository.findAllByChecklistItem_Study_IdOrderByStudyOrderIndexAsc(studyId);
        return list.stream().map(StudyChecklistItemDetailResponse::fromEntity).toList();
    }

    @Override
    public ChecklistItemUnassignResponse unassignChecklistItem(Long checklistId, Long memberId){
        ChecklistItemAssignment cm = checklistItemAssignmentRepository.findByChecklistItemIdAndMemberId(
                        checklistId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));

        return ChecklistItemUnassignResponse.success(checklistId, memberId);
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
