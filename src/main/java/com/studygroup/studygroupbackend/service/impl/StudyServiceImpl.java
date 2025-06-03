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

        Study study = Study.of(request.getName(),request.getDescription());
        studyRepository.save(study);

        StudyMember leaderMember = StudyMember.of(study, leader, StudyRole.Leader);
        studyMemberRepository.save(leaderMember);

        StudyDto.MemberResDto leaderDto = StudyDto.MemberResDto.fromEntity(leaderMember);

        /*
        여기서 memberList넘길건지? -> 화면 생성 후 바로 상세페이지로 들어갈 것이면 필요함
        +
        팀 만들때 초대 가능하면 필요함 -> qr로 하는거면 굳이..?
        */
        return StudyDto.CreateResDto.fromEntity(study, leaderDto);
    }

    @Override
    public StudyDto.DetailResDto getStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        List<StudyMember> studyMembers = studyMemberRepository.findByStudy(study);
        List<StudyDto.MemberResDto> memberDtos = studyMembers.stream()
                .map(StudyDto.MemberResDto::fromEntity)
                .toList();

        StudyDto.MemberResDto leaderDto = memberDtos.stream()
                .filter(m->m.getRole().equals("Leader"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("리더가 존재하지 않습니다."));

        return StudyDto.DetailResDto.fromEntity(study, leaderDto, memberDtos);
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

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.Leader)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));

        StudyDto.MemberResDto leaderDto = StudyDto.MemberResDto.fromEntity(leaderMember);

        study.updateStudyInfo(request.getName(), request.getDescription());



        return StudyDto.UpdateResDto.fromEntity(study, leaderDto);
    }

    @Override
    @Transactional
    public StudyDto.DeleteResDto deleteStudy(Long studyId, Long leaderId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.Leader)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));

        studyMemberRepository.deleteByStudy(study);
        studyRepository.delete(study);

        return StudyDto.DeleteResDto.successDelete();
    }
}
