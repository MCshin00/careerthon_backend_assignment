package com.careerthon.assignment.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final UserExceptionMessage errorCode;

    public UserException(UserExceptionMessage errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
