package com.careerthon.assignment.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    CUSTOMER(Authority.USER),
    SELLER(Authority.ADMIN);

    private final String authority;

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

    @JsonCreator
    public static UserRole fromString(String role) {
        return UserRole.valueOf(role.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
