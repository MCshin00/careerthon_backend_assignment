package com.careerthon.assignment.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
    private ErrorDetail error;

    @Getter
    @Builder
    public static class ErrorDetail {
        private String code;
        private String message;
    }

    public static <T extends ErrorCode> ErrorResponseDto of(T message) {
        return ErrorResponseDto.builder()
                .error(ErrorDetail.builder()
                        .code(message.name())
                        .message(message.getMessage())
                        .build())
                .build();
    }
}
