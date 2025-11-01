package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.*;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyMemberSummaryResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyOrderUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
import com.studygroup.studygroupbackend.repository.*;
import com.studygroup.studygroupbackend.service.StudyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StudyServiceImpl implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyInvitationRepository studyInvitationRepository;
    private final ChecklistItemRepository checklistItemRepository;

    @Override
    @Transactional
    public StudyCreateResponse createStudy(Long leaderId, StudyCreateRequest request) {
        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("스터디 장을 찾을 수 없습니다."));

        Study study = request.toEntity();
        studyRepository.save(study);

        Integer maxPersonalOrderIndex = studyMemberRepository
                .findMaxPersonalOrderIndexByMemberId(leader.getId())
                .orElse(-1);


        StudyMember leaderMember = StudyMember.of(
                study,
                leader,
                request.getColor(),
                StudyRole.LEADER,
                maxPersonalOrderIndex+1,
                LocalDateTime.now()
        );

        studyMemberRepository.save(leaderMember);

        StudyMemberSummaryResponse leaderDto = StudyMemberSummaryResponse.fromEntity(leaderMember);

        return StudyCreateResponse.fromEntity(study, leaderDto);
    }

    @Override
    public StudyDetailResponse getStudyByMemberIdAndStudyId(Long memberId, Long studyId) {
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 StudyMember 엔티티를 찾을 수 업습니다."));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        List<StudyMember> studyMembers = studyMemberRepository.findByStudy(study);

        List<StudyMemberSummaryResponse> memberDtos = studyMembers.stream()
                .map(StudyMemberSummaryResponse::fromEntity)
                .toList();

        StudyMemberSummaryResponse leaderDto = memberDtos.stream()
                .filter(m->m.getRole().equals("LEADER"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("리더가 존재하지 않습니다."));

        return StudyDetailResponse.fromEntity(study, studyMember, leaderDto, memberDtos);
    }

    @Override
    public List<StudyDetailResponse> getStudiesByMemberIdAsc(Long memberId) {
        /*
        memberId 기준으로 Studymember을 조회 (study fetch join 포함), 개인 순서 오름차순
         */
        List<StudyMember> myStudyMembers = studyMemberRepository.findStudyMembersByMemberIdOrderByPersonalOrderIndexAsc(memberId);

        //2. studyId추출
        List<Long> studyIds = myStudyMembers.stream()
                .map(sm -> sm.getStudy().getId())
                .distinct()
                .toList();

        /*
        여러 스터디 ID에 해당하는 StudyMember 전체 조회 (member fetch join 포함)
         */
        List<StudyMember> allStudyMembers = studyMemberRepository.findStudyMembersByStudyIdIn(studyIds);

        /*
        allStudyMembers에서 찾은 studymember들을 studyId를 기준으로 그룹핑
         */
        Map<Long, List<StudyMember>> myStudyMembersByStudyId = allStudyMembers.stream()
                .collect(Collectors.groupingBy(sm -> sm.getStudy().getId()));

        return myStudyMembers.stream()
                .map(myStudyMember -> {
                    Study study = myStudyMember.getStudy();
                    Long studyId = study.getId();

                    List<StudyMember> studyMembersInStudy = myStudyMembersByStudyId.getOrDefault(studyId, List.of());

                    List<StudyMemberSummaryResponse> memberDtos  = studyMembersInStudy.stream()
                            .map(StudyMemberSummaryResponse::fromEntity)
                            .toList();

                    StudyMemberSummaryResponse leaderDto = memberDtos.stream()
                            .filter(m -> m.getRole().equals("LEADER"))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("리더가 존재하지 않습니다."));

                    return StudyDetailResponse.fromEntity(study, myStudyMember, leaderDto, memberDtos);
                }).toList();
    }

    @Override
    @Transactional
    public StudyDetailResponse updateStudy(Long leaderId, StudyUpdateRequest request) {
        Long studyId = request.getStudyId();

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다."));

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 수정할 수 있습니다."));


        StudyMemberSummaryResponse leaderDto = StudyMemberSummaryResponse.fromEntity(leaderMember);

        study.updateStudyInfo(
                request.getName(),
                request.getDescription(),
                request.getPersonalColor(),
                request.getDueDate(),
                request.getStatus()
        );

        leaderMember.updatePersonalColor(request.getPersonalColor());

        return StudyDetailResponse.fromEntity(study, leaderMember, leaderDto, null);
    }

    @Override
    @Transactional
    public void updateStudyOrder(Long memberId, List<StudyOrderUpdateRequest> requests) {
        for (StudyOrderUpdateRequest studyOrderUpdateRequest : requests) {
            studyMemberRepository.updatePersonalOrderIndex(memberId, studyOrderUpdateRequest.getStudyId(), studyOrderUpdateRequest.getPersonalOrderIndex());
        }
    }

    @Override
    @Transactional
    public StudyDeleteResponse deleteStudy(Long studyId, Long leaderId) {
        Study study = studyRepository.findByIdAndDeletedFalse(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디를 찾을 수 없습니다"));

        StudyMember leaderMember = studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
                .orElseThrow(() -> new IllegalStateException("스터디장만 스터디를 삭제할 수 있습니다."));

//일단 스터디에 연결된게, 스터디 멤버, 체크리스트, 인비데이션
        List<StudyMember> members = studyMemberRepository.findByStudyIdAndDeletedFalse(studyId);
        List<StudyInvitation> invitations = studyInvitationRepository.findByStudyIdAndDeletedFalse(studyId);
        List<ChecklistItem> items = checklistItemRepository.findByStudyIdAndDeletedFalse(studyId);

        study.softDeletion();

        members.forEach(StudyMember::softDeletion);
        invitations.forEach(StudyInvitation::softDeletion);
        items.forEach(ChecklistItem::softDeletion);

        return StudyDeleteResponse.successDelete();
    }
}
