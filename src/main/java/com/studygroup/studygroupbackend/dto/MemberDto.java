package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.entity.Member;
import lombok.Builder;
import lombok.Getter;

public class MemberDto {

    // 회원 가입 요청 DTO
    @Getter
    @Builder
    public static class CreateReqDto {
        private final String userName;
        private final String password;
        private final String email;

        public Member toEntity(){
            return Member.of(userName, password, email);
        }
    }

    //회원 가입 응답 DTO
    @Getter
    @Builder
    public static class CreateResDto{
        private final Long id;
    }

    //로그인 요청 DTO
    @Getter
    @Builder
    public static class LoginReqDto {
        private final String userName;
        private final String password;
    }

    //로그인 응답 DTO
    @Getter
    @Builder
    public static class LoginResDto {
        private final Long id;
        private final String userName;
    }

    //회원 상세 조회 응답 DTO
    @Getter
    @Builder
    public static class DetailResDto {
        private final Long id;
        private final String userName;
        private final String email;

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
    public static class ListResDto{
        private final Long id;
        private final String userName;
        private final String email;

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
    public static class UpdateReqDto{
        private final Long id;
        private final String userName;
        private final String email;
    }

    //회원 정보 수정 응답 DTO
    @Getter
    @Builder
    public static class UpdateResDto{
        private final Long id;
        private final String userName;
        private final String email;

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
    public static class DeleteResDto{
        private final String message;


        public static DeleteResDto success() {
            return DeleteResDto.builder()
                    .message("회원 삭제 완료")
                    .build();
        }
    }


}
