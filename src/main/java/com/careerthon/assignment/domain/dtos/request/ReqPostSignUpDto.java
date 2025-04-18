package com.careerthon.assignment.domain.dtos.request;

import com.careerthon.assignment.model.entity.User;
import com.careerthon.assignment.model.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqPostSignUpDto {
    private String username;
    private String password;
    private String nickname;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .role(UserRole.USER)
                .build();
    }
}
