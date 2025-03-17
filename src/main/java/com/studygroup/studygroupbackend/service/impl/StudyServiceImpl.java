package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.StudyDto;
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
    public StudyDto.CreateResDto createStudy(StudyDto.CreateReqDto request) {
        Member leader = memberRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new EntityNotFoundException("스터디 장을 찾을 수 없습니다."));

        Study study = Study.of(request.getName(),request.getDescription(), leader);
        studyRepository.save(study);

        StudyMember leaderMember = StudyMember.of(study, leader, StudyRole.Leader);
        studyMemberRepository.save(leaderMember);

        return StudyDto.CreateResDto.fromEntity(study);
    }

    @Override
    public StudyDto.DetailResDto getStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        List<StudyMember> studyMembers = studyMemberRepository.findByStudy(study);
        List<StudyDto.MemberResDto> memberDtos = studyMembers.stream()
                .map(StudyDto.MemberResDto::fromEntity)
                .toList();

        return StudyDto.DetailResDto.fromEntity(study, memberDtos);
    }

    @Override
    public List<StudyDto.ListResDto> getAllStudies() {
        return studyRepository.findAll().stream()
                .map(StudyDto.ListResDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public StudyDto.UpdateResDto updateStudy(Long studyId, Long leaderId, StudyDto.UpdateReqDto request) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다.");
        }

        study.updateStudyInfo(request.getName(), request.getDescription());

        return StudyDto.UpdateResDto.fromEntity(study);
    }

    @Override
    @Transactional
    public StudyDto.DeleteResDto deleteStudy(Long studyId, Long leaderId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("스터디장만 스터디를 삭제할 수 있습니다.");
        }

        studyMemberRepository.deleteByStudy(study);
        studyRepository.delete(study);

        return StudyDto.DeleteResDto.successDelete();
    }
}
