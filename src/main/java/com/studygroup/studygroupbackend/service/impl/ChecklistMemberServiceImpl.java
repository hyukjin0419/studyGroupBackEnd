package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.ChecklistMemberDto;
import com.studygroup.studygroupbackend.entity.Checklist;
import com.studygroup.studygroupbackend.entity.ChecklistMember;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.repository.ChecklistMemberRepository;
import com.studygroup.studygroupbackend.repository.ChecklistRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
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
    private final ChecklistRepository checklistRepository;
    private final MemberRepository memberRepository;

    @Override
    public ChecklistMemberDto.AssignResDto assignChecklist(ChecklistMemberDto.AssignReqDto request) {
        Checklist checklist = checklistRepository.findById(request.getChecklistId())
                .orElseThrow(() -> new EntityNotFoundException("체크리스트가 존재하지 않습니다"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("멤버가 존재하지 않습니다"));

        ChecklistMember cm = ChecklistMember.assign(checklist, member);
        checklistMemberRepository.save(cm);
        return ChecklistMemberDto.AssignResDto.fromEntity(cm);
    }

    @Override
    public ChecklistMemberDto.ChangeStatusResDto changeStatus(ChecklistMemberDto.ChangeStatusReqDto request) {
        ChecklistMember cm = checklistMemberRepository.findByChecklistIdAndMemberId(
                        request.getChecklistId(), request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));


        cm.changeStatus();

        return ChecklistMemberDto.ChangeStatusResDto.fromEntity(cm);
    }

    @Override
    @Transactional(readOnly = true)//본인 체크리스트 조회용
    public List<ChecklistMemberDto.MemberChecklistResDto> getMemberChecklists(Long memberId) {
        List<ChecklistMember> list = checklistMemberRepository.findAllByMemberId(memberId);
        return list.stream().map(ChecklistMemberDto.MemberChecklistResDto::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)//스터디별 체크리스트 조회용
    public List<ChecklistMemberDto.StudyChecklistMemberResDto> getChecklistMembersByStudyId(Long studyId) {
        List<ChecklistMember> list = checklistMemberRepository.findAllByChecklist_Study_Id(studyId);
        return list.stream().map(ChecklistMemberDto.StudyChecklistMemberResDto::fromEntity).toList();
    }

    @Override
    public ChecklistMemberDto.UnassignResDto unassignChecklist(Long checklistId, Long memberId){
        ChecklistMember cm = checklistMemberRepository.findByChecklistIdAndMemberId(
                        checklistId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("할당된 체크리스트가 없습니다."));

        return ChecklistMemberDto.UnassignResDto.success(checklistId, memberId);
    }

}
