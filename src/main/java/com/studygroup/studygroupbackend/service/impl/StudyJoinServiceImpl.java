package com.studygroup.studygroupbackend.service.impl;


import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyMemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.StudyJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StudyJoinServiceImpl implements StudyJoinService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public void joinStudyByCode(Long memberId, String joinCode) {
        Study study = studyRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new IllegalStateException("유효하지 않는 참여 코드 입니다."));

        boolean isAlreadyJoined = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), memberId);
        if (isAlreadyJoined) throw new IllegalStateException("이미 해당 스터디에 참여한 사용자입니다.");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        int newPersonalOrderIndex = studyMemberRepository
                .findMaxPersonalOrderIndexByMemberId(memberId)
                .orElse(0) + 1;

        StudyMember newMember = StudyMember.of(
                study,
                member,
                study.getColor(),
                StudyRole.FELLOW,
                newPersonalOrderIndex
        );

        studyMemberRepository.save(newMember);
    }
}
