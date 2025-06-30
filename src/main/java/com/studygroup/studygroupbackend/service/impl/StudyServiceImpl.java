package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.MyStudyListResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyMemberSummaryResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyOrderUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.StudyRole;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyMemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import com.studygroup.studygroupbackend.service.StudyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StudyServiceImpl implements StudyService {
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    @Transactional
    public StudyCreateResponse createStudy(Long leaderId, StudyCreateRequest request) {
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("스터디 장을 찾을 수 없습니다."));

        Study study = Study.of(request.getName(),request.getDescription());
        studyRepository.save(study);

        Integer maxPersonalOrderIndex = studyMemberRepository
                .findMaxPersonalOrderIndexByMemberId(leader.getId())
                .orElse(-1);


        StudyMember leaderMember = StudyMember.of(
                study,
                leader,
                StudyRole.LEADER,
                maxPersonalOrderIndex+1
        );

        studyMemberRepository.save(leaderMember);

        StudyMemberSummaryResponse leaderDto = StudyMemberSummaryResponse.fromEntity(leaderMember);

        return StudyCreateResponse.fromEntity(study, leaderDto);
    }

    @Override
    public StudyDetailResponse getStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        List<StudyMember> studyMembers = studyMemberRepository.findByStudy(study);
        List<StudyMemberSummaryResponse> memberDtos = studyMembers.stream()
                .map(StudyMemberSummaryResponse::fromEntity)
                .toList();

        StudyMemberSummaryResponse leaderDto = memberDtos.stream()
                .filter(m->m.getRole().equals("Leader"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("리더가 존재하지 않습니다."));

        return StudyDetailResponse.fromEntity(study, leaderDto, memberDtos);
    }

    @Override
    public List<StudyListResponse> getAllStudies() {
        return studyRepository.findAll().stream()
                .map(StudyListResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public StudyDetailResponse updateStudy(Long studyId, Long leaderId, StudyUpdateRequest request) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));

        StudyMemberSummaryResponse leaderDto = StudyMemberSummaryResponse.fromEntity(leaderMember);

        study.updateStudyInfo(request.getName(), request.getDescription());

        return StudyDetailResponse.fromEntity(study, leaderDto, null);
    }

    @Override
    @Transactional
    public StudyDeleteResponse deleteStudy(Long studyId, Long leaderId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));

        studyMemberRepository.deleteByStudy(study);
        studyRepository.delete(study);

        return StudyDeleteResponse.successDelete();
    }


    @Override
    public List<StudyListResponse> getStudiesByMemberId(Long memberId) {
        List<StudyMember> studyMembers = studyMemberRepository.findByMemberId(memberId);

        return studyMembers.stream()
                .map(sm -> StudyListResponse.fromEntity(sm.getStudy()))
                .toList();
    }

    @Override
    public List<MyStudyListResponse> getStudiesByMemberIdAsc(Long memberId) {
        List<StudyMember> studyMembers = studyMemberRepository.findByMemberIdOrderByPersonalOrderIndexAsc(memberId);
        return studyMembers.stream()
                .map(MyStudyListResponse::fromStudyMember)
                .toList();
    }

    @Override
    @Transactional
    public void updateStudyOrder(Long memberId, List<StudyOrderUpdateRequest> requests) {
        for (StudyOrderUpdateRequest studyOrderUpdateRequest : requests) {
            studyMemberRepository.updatePersonalOrderIndex(memberId, studyOrderUpdateRequest.getStudyId(), studyOrderUpdateRequest.getPersonalOrderIndex());
        }
    }
}
