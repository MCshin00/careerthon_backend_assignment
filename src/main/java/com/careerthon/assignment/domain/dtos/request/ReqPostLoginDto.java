package com.careerthon.assignment.domain.dtos.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostLoginDto {
    private String username;
    private String password;
}
