package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StudyMemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InviteReqDto{
        private Long memberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InviteResDto{
        private Long studyId;
        private Long memberId;
        private String role;

        public static InviteResDto fromEntity(StudyMember studyMember) {
            return InviteResDto.builder()
                    .studyId(studyMember.getStudy().getId())
                    .memberId(studyMember.getMember().getId())
                    .role(studyMember.getStudyRole().name())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemoveResDto{
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
