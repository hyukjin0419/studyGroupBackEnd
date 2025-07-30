package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyMemberInvitationRequest;

import java.util.List;

public interface StudyJoinService {
    void joinStudyByCode(Long memberId, String joinCode);
    void inviteMember(Long leaderId, Long studyId, String inviteeUuid);
    void inviteMembers(Long leaderId, Long studyId, List<StudyMemberInvitationRequest> requestList);
    Long acceptInvitation(Long invitationId, Long memberId);
}
