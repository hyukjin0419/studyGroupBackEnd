package com.studygroup.studygroupbackend.dto.studyJoin.leader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMemberInvitationRequest {
    private Long inviteeId;
}
