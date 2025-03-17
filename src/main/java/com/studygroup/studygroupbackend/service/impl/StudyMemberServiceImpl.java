package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.StudyMemberDto;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.StudyMember;
import com.studygroup.studygroupbackend.entity.StudyRole;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyMemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.StudyMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyMemberServiceImpl implements StudyMemberService{
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    public StudyMemberDto.InviteResDto inviteMember(Long studyId, Long leaderId, StudyMemberDto.InviteReqDto request) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("스터디장만 멤버를 초대할 수 있습니다");
        }

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        if (studyMemberRepository.existsByStudyAndMember(study, member)) {
            throw new IllegalStateException("이미 스터디에 가입된 멤버입니다.");
        }

        StudyMember studyMember = StudyMember.of(study, member, StudyRole.Fellow);
        studyMemberRepository.save(studyMember);

        return StudyMemberDto.InviteResDto.fromEntity(studyMember);
    }

    @Override
    public StudyMemberDto.RemoveResDto removeMember(Long studyId, Long leaderId, Long memberId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("스터디장만 멤버를 삭제할 수 있습니다");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        StudyMember studyMember = studyMemberRepository.findByStudyAndMember(study, member)
                .orElseThrow(() -> new EntityNotFoundException("스터디에 해당 멤버가 없습니다."));

        studyMemberRepository.delete(studyMember);

        return StudyMemberDto.RemoveResDto.successDelete(studyId, memberId);
    }
}
