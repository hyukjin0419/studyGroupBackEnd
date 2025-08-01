//package com.studygroup.studygroupbackend.service.impl;
//
//import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyMemberInvitationRequest;
//import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyInvitationResponse;
//import com.studygroup.studygroupbackend.dto.studyJoin.StudyMemberRemoveResponse;
//import com.studygroup.studygroupbackend.domain.Member;
//import com.studygroup.studygroupbackend.domain.Study;
//import com.studygroup.studygroupbackend.domain.StudyMember;
//import com.studygroup.studygroupbackend.domain.status.StudyRole;
//import com.studygroup.studygroupbackend.repository.MemberRepository;
//import com.studygroup.studygroupbackend.repository.StudyMemberRepository;
//import com.studygroup.studygroupbackend.repository.StudyRepository;
//import com.studygroup.studygroupbackend.service.StudyMemberService;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class StudyMemberServiceImpl implements StudyMemberService{
//    private final MemberRepository memberRepository;
//    private final StudyRepository studyRepository;
//    private final StudyMemberRepository studyMemberRepository;
//
//    @Override
//    public StudyInvitationResponse inviteMember(Long studyId, Long leaderId, StudyMemberInvitationRequest request) {
//        Study study = studyRepository.findById(studyId)
//                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));
//
//        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
//                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));
//
//        Member member = memberRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));
//
//        if (studyMemberRepository.existsByStudyAndMember(study, member)) {
//            throw new IllegalStateException("이미 스터디에 가입된 멤버입니다.");
//        }
//
//        Integer maxPersonalOrderIndex = studyMemberRepository
//                .findMaxPersonalOrderIndexByMemberId(member.getId())
//                .orElse(-1);
//
//        StudyMember studyMember = StudyMember.of(study, member, study.getColor(), StudyRole.FELLOW, maxPersonalOrderIndex);
//        studyMemberRepository.save(studyMember);
//
//        return StudyInvitationResponse.fromEntity(studyMember);
//    }
//
//    @Override
//    public StudyMemberRemoveResponse removeMember(Long studyId, Long leaderId, Long memberId) {
//        Study study = studyRepository.findById(studyId)
//                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));
//
//        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
//                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));
//
////        StudyMember studyMember = studyMemberRepository.findByStudyAndMember(study, member)
////                .orElseThrow(() -> new EntityNotFoundException("스터디에 해당 멤버가 없습니다."));
//
////        studyMemberRepository.delete(studyMember);
//
//        return StudyMemberRemoveResponse.successDelete(studyId, memberId);
//    }
//}
//
