package com.careerthon.assignment.domain.service;

import com.careerthon.assignment.domain.dtos.request.ReqPostLoginDto;
import com.careerthon.assignment.domain.dtos.request.ReqPostSignUpDto;
import com.careerthon.assignment.domain.dtos.response.ResPatchToAdminDto;
import com.careerthon.assignment.domain.dtos.response.ResPostLoginDto;
import com.careerthon.assignment.domain.dtos.response.ResPostSignUpDto;
import com.careerthon.assignment.exception.UserException;
import com.careerthon.assignment.exception.UserExceptionMessage;
import com.careerthon.assignment.model.entity.User;
import com.careerthon.assignment.model.entity.UserRole;
import com.careerthon.assignment.model.repository.UserRepository;
import com.careerthon.assignment.util.JwtUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResPostSignUpDto signup(ReqPostSignUpDto requestDto) {
        checkUsername(requestDto.getUsername());

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = requestDto.toEntity(encodedPassword);
        userRepository.save(user);

        return ResPostSignUpDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();
    }

    public ResPostLoginDto login(ReqPostLoginDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .filter(u -> passwordEncoder.matches(requestDto.getPassword(), u.getPassword()))
                .orElseThrow(() -> new UserException(UserExceptionMessage.INVALID_CREDENTIALS));

        String token = jwtUtil.createToken(user.getId(), user.getUsername(), user.getRole());

        return ResPostLoginDto.builder()
                .token(token)
                .build();
    }

    @Transactional
    public ResPatchToAdminDto toAdmin(UUID userId, UserRole role) {
        checkRole(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionMessage.USER_NOT_FOUND));

        user.updateRole(UserRole.ADMIN);

        return ResPatchToAdminDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .role(role)
                .build();
    }

    private void checkUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserException(UserExceptionMessage.USER_ALREADY_EXISTS);
        }
    }

    private void checkRole(UserRole role) {
        if (!role.equals(UserRole.ADMIN)) {
            throw new UserException(UserExceptionMessage.ACCESS_DENIED);
        }
    }
}
