package com.careerthon.assignment.domain.dtos.response;

import com.careerthon.assignment.model.entity.UserRole;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResPostSignUpDto {
    private UUID userId;
    private String username;
    private String nickname;
    private UserRole role;
}
