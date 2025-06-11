package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyMemberSummaryResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.StudyMember;
import com.studygroup.studygroupbackend.entity.StudyRole;
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
    public StudyCreateResponse createStudy(StudyCreateRequest request) {
        Member leader = memberRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new EntityNotFoundException("스터디 장을 찾을 수 없습니다."));

        Study study = Study.of(request.getName(),request.getDescription());
        studyRepository.save(study);

        StudyMember leaderMember = StudyMember.of(study, leader, StudyRole.Leader);
        studyMemberRepository.save(leaderMember);

        StudyMemberSummaryResponse leaderDto = StudyMemberSummaryResponse.fromEntity(leaderMember);

        /*
        여기서 memberList넘길건지? -> 화면 생성 후 바로 상세페이지로 들어갈 것이면 필요함
        +
        팀 만들때 초대 가능하면 필요함 -> qr로 하는거면 굳이..?
        */
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

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.Leader)
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

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.Leader)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));

        studyMemberRepository.deleteByStudy(study);
        studyRepository.delete(study);

        return StudyDeleteResponse.successDelete();
    }
}
