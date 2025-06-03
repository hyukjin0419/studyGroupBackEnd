package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class StudyMemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InviteReqDto{
        private String email;
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InviteResDto extends BaseResDto{
        private Long studyId;
        private Long memberId;
        private String userName;
        private String role;
        private LocalDateTime joinedAt;

        public static InviteResDto fromEntity(StudyMember studyMember) {
            return InviteResDto.builder()
                    .studyId(studyMember.getStudy().getId())
                    .memberId(studyMember.getMember().getId())
                    .userName(studyMember.getMember().getUserName())
                    .role(studyMember.getStudyRole().name())
                    .joinedAt(studyMember.getJoinedAt())
                    .createdAt(studyMember.getCreatedAt())
                    .modifiedAt(studyMember.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemoveResDto {
        private Long studyId;
        private Long memberId;
        private String message;

        public static RemoveResDto successDelete(Long studyId, Long memberId) {
            return RemoveResDto.builder()
                    .studyId(studyId)
                    .memberId(memberId)
                    .message("스터디에서 멤버가 삭제되었습니다.")
                    .build();
        }
    }
}
