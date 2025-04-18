package com.careerthon.assignment.domain.dtos.response;

import com.careerthon.assignment.exception.UserExceptionMessage;
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

    public static ErrorResponseDto of(UserExceptionMessage message) {
        return ErrorResponseDto.builder()
                .error(ErrorDetail.builder()
                        .code(message.name())
                        .message(message.getMessage())
                        .build())
                .build();
    }
}
