package com.careerthon.assignment.domain.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostLoginDto {
    private String token;
}
