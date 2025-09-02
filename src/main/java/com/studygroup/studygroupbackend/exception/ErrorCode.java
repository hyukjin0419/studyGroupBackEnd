package com.studygroup.studygroupbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //=== 로그인 시 ===//
    AUTH_WRONG_PASSWORD("AUTH_WRONG_PASSWORD", HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않아요. 다시 입력하거나 재설정할 수 있어요."),
    AUTH_MEMBER_NOT_FOUND("AUTH_MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "이메일 또는 아이디를 다시 확인해 주세요. 처음이시라면 회원가입을 진행할 수 있어요.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


}
