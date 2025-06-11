package com.studygroup.studygroupbackend.dto.member.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    private Long id;
    private String userName;
    private String email;
}