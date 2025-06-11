package com.studygroup.studygroupbackend.dto.studymember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyMemberInviteRequest {
    private String email;
}
