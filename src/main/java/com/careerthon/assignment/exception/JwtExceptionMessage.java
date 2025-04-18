package com.careerthon.assignment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionMessage {
    INVALID_TOKEN("유효하지 않은 인증 토큰입니다.");

    private final String message;
}
