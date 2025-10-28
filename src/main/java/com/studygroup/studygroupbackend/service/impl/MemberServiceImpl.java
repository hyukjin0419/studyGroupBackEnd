package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.search.MemberSearchResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberEmailUpdateRequest;
import com.studygroup.studygroupbackend.dto.member.update.MemberDisplayNameUpdateRequest;
import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.exception.BusinessException;
import com.studygroup.studygroupbackend.exception.ErrorCode;
import com.studygroup.studygroupbackend.repository.*;
import com.studygroup.studygroupbackend.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyInvitationRepository studyInvitationRepository;
    private final StudyRepository studyRepository;
    private final ChecklistItemRepository checklistItemRepository;

    @Override
    public MemberDetailResponse getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        return MemberDetailResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDetailResponse updateMyDisplayName(Long memberId, MemberDisplayNameUpdateRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        member.updateDisplayName(request.getDisplayName());

        return MemberDetailResponse.fromEntity(member);
    }


    @Override
    @Transactional
    public MemberDetailResponse updateMemberEmail(Long memberId, MemberEmailUpdateRequest request) {
        validateDuplicateEmail(request.getEmail());

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        member.updateEmail(request.getEmail());

        return MemberDetailResponse.fromEntity(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.UPDATE_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public MemberDeleteResponse deleteMember(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));

        member.softDeletion();

        checklistItemRepository.softDeleteAllByMemberId(memberId);
        studyRepository.softDeleteAllByLeaderMemberId(memberId);
        studyMemberRepository.softDeleteAllByMemberId(memberId);

        return MemberDeleteResponse.successDelete();
    }

    @Override
    public List<MemberDetailResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    public List<MemberSearchResponse> searchAvailableMembersByUserName(String keyword, Long studyId) {
        if(keyword == null || keyword.trim().isEmpty()){
            return Collections.emptyList();
        }

        List<Long> joinedMemberIds = studyMemberRepository.findMemberIdsByStudyId(studyId);

        List<Long> invitedmembersIds = studyInvitationRepository.findInviteeIdsByStudyIdAndStatus(studyId, InvitationStatus.PENDING);

        Set<Long> excludedIds = new HashSet<>(joinedMemberIds);
        excludedIds.addAll(invitedmembersIds);

        List<Member> members = memberRepository
                .findByUserNameContainingIgnoreCaseAndIdNotInOrderByUserNameAsc(keyword, excludedIds);

        return members.stream()
                .map(MemberSearchResponse::fromEntity)
                .toList();
    }
}
