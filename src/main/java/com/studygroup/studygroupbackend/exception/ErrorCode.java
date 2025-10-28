package com.studygroup.studygroupbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // === 로그인 시 === //
    AUTH_WRONG_PASSWORD("AUTH_WRONG_PASSWORD", HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않아요. 다시 입력하거나 재설정할 수 있어요.", true),
    AUTH_MEMBER_NOT_FOUND("AUTH_MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "이메일과 비밀번호를 다시 확인해 주세요.\n처음이시라면 회원가입을 진행할 수 있어요.", true),

    // === 회원 가입시 (Member) 관련 ===//
    AUTH_USERNAME_ALREADY_EXISTS("AUTH_USERNAME_ALREADY_EXISTS", HttpStatus.CONFLICT, "이미 사용중인 아이디입니다. 다른 아이디로 가입을 도와드릴게요.", true),
    AUTH_EMAIL_ALREADY_EXISTS("MEMBER_EMAIL_EXISTS", HttpStatus.CONFLICT, "이미 사용중인 이메일입니다. 다른 이메일으로 가입을 도와드릴게요.", true),

    // === 회원 정보 변경시 관련 ===//
    UPDATE_USERNAME_ALREADY_EXISTS("UPDATE_USERNAME_ALREADY_EXISTS", HttpStatus.CONFLICT, "이미 사용중인 아이디입니다. 다른 아이디로 변경을 도와드릴게요.", true),
    UPDATE_EMAIL_ALREADY_EXISTS("UPDATE_MEMBER_EMAIL_EXISTS", HttpStatus.CONFLICT, "이미 사용중인 이메일입니다. 다른 이메일으로 변경을 도와드릴게요.", true);




    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    private final boolean clientVisible;
}
