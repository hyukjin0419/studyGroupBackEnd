package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    // 회원 가입 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReqDto {
        private String userName;
        private String password;
        private String email;

        public Member toEntity(){
            return Member.of(userName, password, email);
        }
    }

    //회원 가입 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResDto{
        private Long id;
    }

    //로그인 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginReqDto {
        private String userName;
        private String password;
    }

    //로그인 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDto {
        private Long id;
        private String userName;
    }

    //회원 상세 조회 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResDto {
        private Long id;
        private String userName;
        private String email;

        public static DetailResDto fromEntity(Member member) {
            return DetailResDto.builder()
                    .id(member.getId())
                    .userName(member.getUserName())
                    .email(member.getEmail())
                    .build();
        }
    }

    //회원 목록 조회 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResDto{
        private Long id;
        private String userName;
        private String email;

        public static ListResDto fromEntity(Member member) {
            return ListResDto.builder()
                    .id(member.getId())
                    .userName(member.getUserName())
                    .email(member.getEmail())
                    .build();
        }
    }

    //회원 정보 수정 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReqDto{
        private Long id;
        private String userName;
        private String email;
    }

    //회원 정보 수정 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResDto{
        private Long id;
        private String userName;
        private String email;

        public static UpdateResDto fromEntity(Member member) {
            return UpdateResDto.builder()
                    .id(member.getId())
                    .userName(member.getUserName())
                    .email(member.getEmail())
                    .build();
        }
    }

    //회원 삭제 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResDto{
        private String message;


        public static DeleteResDto success() {
            return DeleteResDto.builder()
                    .message("회원 삭제 완료")
                    .build();
        }
    }
}
