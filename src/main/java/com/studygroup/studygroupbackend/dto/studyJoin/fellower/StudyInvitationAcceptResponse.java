package com.studygroup.studygroupbackend.dto.studyJoin.fellower;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudyInvitationAcceptResponse {
    private Long studyId;

    public static StudyInvitationAcceptResponse from(Long studyId){
        return StudyInvitationAcceptResponse.builder()
                .studyId(studyId)
                .build();
    }
}
