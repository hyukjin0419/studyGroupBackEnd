package com.studygroup.studygroupbackend.dto.member.search;

import com.studygroup.studygroupbackend.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchResponse {
    private String uuid;
    private String userName;

    public static MemberSearchResponse fromEntity(Member member){
        return MemberSearchResponse.builder()
                .uuid(member.getUuid())
                .userName(member.getUserName())
                .build();
    }
}
