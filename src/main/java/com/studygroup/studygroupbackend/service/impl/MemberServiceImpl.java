package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.search.MemberSearchResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberUpdateRequest;
import com.studygroup.studygroupbackend.domain.Member;
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
    public MemberDetailResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        member.updateProfile(request.getUserName(),request.getEmail());

        return MemberDetailResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDeleteResponse deleteMember(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));

        member.softDeletion();

        //멤버와 연관관계에 있는 studyMember 전부 삭제
        //이렇게 되면 기존에 속해있던팀은 어떻게 되나?
        //response 돌려줄 때 delete는 빼고 주어야 한다 -> SQL strict로 해결
        //여기서 부터는 JPQL로 작성하자
        //순서가 지켜져야 함. ci와 s가 을 참조하고 있기 때문
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
