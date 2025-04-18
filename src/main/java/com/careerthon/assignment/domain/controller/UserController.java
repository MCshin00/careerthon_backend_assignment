package com.careerthon.assignment.domain.controller;

import com.careerthon.assignment.domain.dtos.request.ReqPostLoginDto;
import com.careerthon.assignment.domain.dtos.request.ReqPostSignUpDto;
import com.careerthon.assignment.domain.dtos.response.ResPatchToAdminDto;
import com.careerthon.assignment.domain.dtos.response.ResPostLoginDto;
import com.careerthon.assignment.domain.dtos.response.ResPostSignUpDto;
import com.careerthon.assignment.domain.service.UserService;
import com.careerthon.assignment.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<ResPostSignUpDto> signup(@RequestBody ReqPostSignUpDto reqPostSignUpDto) {
        ResPostSignUpDto responseDto = userService.signup(reqPostSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("login")
    public ResponseEntity<ResPostLoginDto> login(@RequestBody ReqPostLoginDto reqPostLoginDto) {
        ResPostLoginDto responseDto = userService.login(reqPostLoginDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("admin/users/{userId}/roles")
    public ResponseEntity<ResPatchToAdminDto> toAdmin(@PathVariable("userId") UUID userId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResPatchToAdminDto responseDto = userService.toAdmin(userId, userDetails.getRole());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
