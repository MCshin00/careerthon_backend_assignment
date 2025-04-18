package com.careerthon.assignment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage {
    USER_ALREADY_EXISTS("이미 가입된 사용자입니다.");

    private final String message;
}
