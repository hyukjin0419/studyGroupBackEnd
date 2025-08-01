package com.studygroup.studygroupbackend.service.impl;


import com.studygroup.studygroupbackend.domain.*;
import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import com.studygroup.studygroupbackend.domain.status.NotificationType;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyMemberInvitationRequest;
import com.studygroup.studygroupbackend.fcm.FcmInvitationRequest;
import com.studygroup.studygroupbackend.fcm.FcmService;
import com.studygroup.studygroupbackend.repository.*;
import com.studygroup.studygroupbackend.service.StudyJoinService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudyJoinServiceImpl implements StudyJoinService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;
    private final StudyInvitationRepository studyInvitationRepository;
    private final AppNotificationRepository appNotificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final FcmService fcmService;

    @Override
    public void joinStudyByCode(Long memberId, String joinCode) {
        Study study = studyRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new IllegalStateException("유효하지 않는 참여 코드 입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        boolean isAlreadyJoined = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), memberId);
        if (isAlreadyJoined) throw new IllegalStateException("이미 해당 스터디에 참여한 사용자입니다.");


        int newPersonalOrderIndex = studyMemberRepository
                .findMaxPersonalOrderIndexByMemberId(memberId)
                .orElse(0) + 1;

        StudyMember newMember = StudyMember.of(
                study,
                member,
                study.getColor(),
                StudyRole.FELLOW,
                newPersonalOrderIndex,
                LocalDateTime.now()
        );

        studyMemberRepository.save(newMember);
    }

    @Override
    public void inviteMember(Long leaderId, Long studyId, String inviteeUuid) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 스터디입니다."));

        studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(studyId, leaderId, StudyRole.LEADER)
                .orElseThrow(() -> new IllegalStateException("스터디 리더만 초대할 수 있습니다."));

        Member inviterMember = memberRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("리더 멤버가 존재하지 않습니다."));

        Member inviteeMember = memberRepository.findByUuid(inviteeUuid)
                .orElseThrow(() -> new EntityNotFoundException("초대 대상 멤버가 존재하지 않습니다."));

        if (inviterMember.getUuid().equals(inviteeUuid)) throw new IllegalStateException("자기 자신을 초대할 수 없습니다.");

        boolean isAlreadyMember = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), inviteeMember.getId());
        if (isAlreadyMember) throw new IllegalStateException("이미 해당 스터디에 참여한 사용자입니다.");

        boolean alreadySentInvitation = studyInvitationRepository.existsByStudyIdAndInviteeIdAndStatus(
                studyId, inviteeMember.getId(), InvitationStatus.PENDING
        );
        if(alreadySentInvitation) throw new IllegalStateException("이미 초대한 멤버입니다.");

        String notificationMessage = inviterMember.getUserName() + "님이 " + inviteeMember.getUserName() + "님을 " + study.getName() + "에 초대하셨습니다.";

        StudyInvitation invitation = StudyInvitation.of(
                study,inviterMember,
                inviteeMember,
                InvitationStatus.PENDING,
                notificationMessage
        );
        studyInvitationRepository.save(invitation);

        AppNotification notification = AppNotification.of(
                inviterMember,
                inviteeMember,
                NotificationType.STUDY_INVITATION,
                invitation.getId(),
                notificationMessage
        );

        appNotificationRepository.save(notification);

        List<String> fcmTokens = deviceTokenRepository.findFcmTokensByMemberId(inviteeMember.getId());

        FcmInvitationRequest fcmInvitationRequest;

        for(String token : fcmTokens){
            fcmInvitationRequest = FcmInvitationRequest.builder()
                    .fcmToken(token)
                    .title("스터디 초대 알림")
                    .body(notificationMessage)
                    .invitationId(invitation.getId())
                    .type(notification.getType())
                    .build();

            fcmService.sendInvitationPush(fcmInvitationRequest);
        }
    }

    @Override
    public void inviteMembers(Long leaderId, Long studyId, List<StudyMemberInvitationRequest> requestList) {
        for (StudyMemberInvitationRequest request : requestList) {
            inviteMember(leaderId, studyId, request.getInviteeUuid());
        }
    }

    @Override
    public Long acceptInvitation(Long invitationId, Long memberId) {
        StudyInvitation invitation = studyInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("초대를 찾을 수 없습니다."));

        Member invitee = invitation.getInvitee();
        Study study = invitation.getStudy();

        boolean isAlreadyJoined = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), invitee.getId());
        if (isAlreadyJoined) throw new IllegalStateException("이미 해당 스터디에 참여한 사용자입니다.");

        if(!invitee.getId().equals(memberId)){
            throw new AccessDeniedException("본인의 초대만 수락할 수 있습니다");
        }

        invitation.accept();

        int newPersonalOrderIndex = studyMemberRepository
                .findMaxPersonalOrderIndexByMemberId(invitee.getId())
                .orElse(0) + 1;

        StudyMember newMember = StudyMember.of(
                study,
                invitee,
                study.getColor(),
                StudyRole.FELLOW,
                newPersonalOrderIndex,
                LocalDateTime.now()
        );

        studyMemberRepository.save(newMember);

        return study.getId();
    }


    @Override
    public void declineInvitation(Long invitationId, Long memberId) {
        StudyInvitation invitation = studyInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new EntityNotFoundException("초대를 찾을 수 없습니다."));

        Member invitee = invitation.getInvitee();
        Study study = invitation.getStudy();

        boolean isAlreadyJoined = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), invitee.getId());
        if (isAlreadyJoined) throw new IllegalStateException("이미 해당 스터디에 참여한 사용자입니다.");

        if(!invitee.getId().equals(memberId)){
            throw new AccessDeniedException("본인의 초대만 거절할 수 있습니다");
        }

        invitation.decline();
    }
}

























