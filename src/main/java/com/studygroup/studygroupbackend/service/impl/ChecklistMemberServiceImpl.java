package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.entity.ChecklistItem;
import com.studygroup.studygroupbackend.entity.ChecklistMember;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.repository.ChecklistMemberRepository;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.ChecklistMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChecklistMemberServiceImpl implements ChecklistMemberService {

    private final ChecklistMemberRepository checklistMemberRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    @Override
    public ChecklistMemberDto.AssignResDto assignChecklist(ChecklistMemberDto.AssignReqDto request) {
        ChecklistItem checklistItem = checklistItemRepository.findById(request.getChecklistId())
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다"));
        Study study = studyRepository.findById(request.getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다"));


        int studyOrder = checklistMemberRepository.findMaxStudyOrderIndex(checklistItem.getStudy().getId()).orElse(0)+1;
        int personalOrder = checklistMemberRepository.findMaxPersonalOrderIndex(member.getId()).orElse(0)+1;


        ChecklistMember cm = ChecklistMember.assign(checklistItem, member, study, studyOrder,personalOrder);
        checklistMemberRepository.save(cm);
        return ChecklistMemberDto.AssignResDto.fromEntity(cm);
    }

    @Override
    public ChecklistMemberDto.ChangeStatusResDto changeStatus(ChecklistMemberDto.ChangeStatusReqDto request) {
        ChecklistMember cm = checklistMemberRepository.findByChecklistItemIdAndMemberId(
                        request.getChecklistId(), request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));


        cm.changeStatus();

        return ChecklistMemberDto.ChangeStatusResDto.fromEntity(cm);
    }

    @Override
    @Transactional(readOnly = true)//본인 체크리스트 조회용
    public List<ChecklistMemberDto.MemberChecklistResDto> getMemberChecklists(Long memberId) {
        List<ChecklistMember> list = checklistMemberRepository.findAllByMemberIdOrderByPersonalOrderIndexAsc(memberId);
        return list.stream().map(ChecklistMemberDto.MemberChecklistResDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)//스터디별 체크리스트 조회용
    public List<ChecklistMemberDto.StudyChecklistMemberResDto> getChecklistMembersByStudyId(Long studyId) {
        List<ChecklistMember> list = checklistMemberRepository.findAllByChecklistItem_Study_IdOrderByStudyOrderIndexAsc(studyId);
        return list.stream().map(ChecklistMemberDto.StudyChecklistMemberResDto::fromEntity).toList();
    }

    @Override
    public ChecklistMemberDto.UnassignResDto unassignChecklist(Long checklistId, Long memberId){
        ChecklistMember cm = checklistMemberRepository.findByChecklistItemIdAndMemberId(
                        checklistId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));

        return ChecklistMemberDto.UnassignResDto.success(checklistId, memberId);
    }

    @Override
    public void updateStudyOrderIndex(Long checklistMemberId, int newIndex) {
        ChecklistMember cm = checklistMemberRepository.findById(checklistMemberId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistMember not found"));
        cm.updateStudyOrderIndex(newIndex);
    }

    @Override
    public void updatePersonalOrderIndex(Long checklistMemberId, int newIndex) {
        ChecklistMember cm = checklistMemberRepository.findById(checklistMemberId)
                .orElseThrow(() -> new EntityNotFoundException("ChecklistMember not found"));
        cm.updatePersonalOrderIndex(newIndex);
    }
}
