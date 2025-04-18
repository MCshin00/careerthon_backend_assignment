package com.careerthon.assignment.domain.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
    private String code;
    private String message;
}
