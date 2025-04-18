package com.careerthon.assignment.domain.service;

import com.careerthon.assignment.domain.dtos.request.ReqPostSignUpDto;
import com.careerthon.assignment.domain.dtos.response.ResPostSignUpDto;
import com.careerthon.assignment.model.entity.User;
import com.careerthon.assignment.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResPostSignUpDto signup(ReqPostSignUpDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = requestDto.toEntity(encodedPassword);
        userRepository.save(user);

        return ResPostSignUpDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }

}
