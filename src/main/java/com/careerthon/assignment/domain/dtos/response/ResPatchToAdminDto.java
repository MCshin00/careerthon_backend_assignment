package com.careerthon.assignment.domain.dtos.response;

import com.careerthon.assignment.model.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPatchToAdminDto {
    private String username;
    private String nickname;
    private UserRole role;
}
