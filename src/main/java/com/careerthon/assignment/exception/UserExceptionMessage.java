package com.careerthon.assignment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage {
    USER_ALREADY_EXISTS("이미 가입된 사용자입니다."),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.");

    private final String message;
}
