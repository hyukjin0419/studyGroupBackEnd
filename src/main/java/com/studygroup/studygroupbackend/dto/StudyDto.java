package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

public class StudyDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateReqDto{
        private String name;
        private String description;
        private Long leaderId;
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateResDto extends BaseResDto{
        private Long id;
        private String name;
        private String description;
        private Long leaderId;
        private String leaderName;

        public static CreateResDto fromEntity(Study study, MemberResDto leaderDto) {
            return CreateResDto.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .description(study.getDescription())
                    .leaderId(leaderDto.getId())
                    .leaderName(leaderDto.getUserName())
                    .createdAt(study.getCreatedAt())
                    .modifiedAt(study.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailResDto extends BaseResDto{
        private Long id;
        private String name;
        private String description;
        private Long leaderId;
        private String leaderName;
        private List<MemberResDto> members;

        public static DetailResDto fromEntity(Study study, MemberResDto leaderDto, List<MemberResDto> members) {
            return DetailResDto.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .description(study.getDescription())
                    .leaderId(leaderDto.getId())
                    .leaderName(leaderDto.getUserName())
                    .members(members)
                    .createdAt(study.getCreatedAt())
                    .modifiedAt(study.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListResDto extends BaseResDto{
        private Long id;
        private String name;
        private String description;

        public static ListResDto fromEntity(Study study) {
            return ListResDto.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .description(study.getDescription())
                    .createdAt(study.getCreatedAt())
                    .modifiedAt(study.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberResDto{
        private Long id;
        private String userName;
        private String role;

        public static MemberResDto fromEntity(StudyMember studyMember) {
            return MemberResDto.builder()
                    .id(studyMember.getMember().getId())
                    .userName(studyMember.getMember().getUserName())
                    .role(studyMember.getStudyRole().name())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateReqDto{
        private String name;
        private String description;
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateResDto extends BaseResDto{
        private Long id;
        private String name;
        private String description;
        private Long leaderId;
        private String leaderName;

        public static UpdateResDto fromEntity(Study study, MemberResDto leaderDto) {
            return UpdateResDto.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .description(study.getDescription())
                    .leaderId(leaderDto.getId())
                    .leaderName(leaderDto.getUserName())
                    .createdAt(study.getCreatedAt())
                    .modifiedAt(study.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteResDto{
        private String message;

        public static DeleteResDto successDelete(){
            return DeleteResDto.builder()
                    .message("스터디 삭제 완료")
                    .build();
        }
    }
}
