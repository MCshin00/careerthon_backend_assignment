package com.careerthon.assignment.exception;

import com.careerthon.assignment.domain.dtos.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "UserExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleUserException(UserException ex) {
        log.warn("UserException 발생: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorResponseDto.of(ex.getErrorCode()));
    }
}