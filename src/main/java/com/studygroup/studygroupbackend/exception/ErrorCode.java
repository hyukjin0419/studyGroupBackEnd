package com.studygroup.studygroupbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // === 로그인 시 === //
    AUTH_WRONG_PASSWORD("AUTH_WRONG_PASSWORD", HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않아요. 다시 입력하거나 재설정할 수 있어요.", true),
    AUTH_MEMBER_NOT_FOUND("AUTH_MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "이메일 또는 아이디를 다시 확인해 주세요. 처음이시라면 회원가입을 진행할 수 있어요.", true),

    // === 회원 가입시 (Member) 관련 ===//
    AUTH_MEMBER_ALREADY_EXISTS("MEMBER_ALREADY_EXISTS", HttpStatus.CONFLICT, "이미 등록된 계정입니다. 다른 계정으로 가입을 도와드릴게요.", true);





    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    private final boolean clientVisible;
}
