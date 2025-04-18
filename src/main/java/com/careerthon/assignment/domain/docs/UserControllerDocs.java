package com.careerthon.assignment.domain.docs;

import com.careerthon.assignment.domain.dtos.request.ReqPostLoginDto;
import com.careerthon.assignment.domain.dtos.request.ReqPostSignUpDto;
import com.careerthon.assignment.domain.dtos.response.ResPatchToAdminDto;
import com.careerthon.assignment.domain.dtos.response.ResPostLoginDto;
import com.careerthon.assignment.domain.dtos.response.ResPostSignUpDto;
import com.careerthon.assignment.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "User API")
public interface UserControllerDocs {
    // 회원가입
    @Operation(summary = "회원가입", description = "회원가입을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = ResPostSignUpDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {
                            @Content(mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "회원정보 중복",
                                                    value = "{\"error\": { \"code\": \"USER_ALREADY_EXISTS\", \"message\": \"이미 가입된 사용자입니다.\" } }")
                                    })
                    })

    })
    @PostMapping("/signup")
    ResponseEntity<ResPostSignUpDto> signup(@RequestBody ReqPostSignUpDto dto);

    // 로그인
    @Operation(summary = "로그인", description = "로그인을 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ResPostLoginDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {
                            @Content(mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "로그인 실패",
                                                    value = "{\"error\": { \"code\": \"INVALID_CREDENTIALS\", \"message\": \"아이디 또는 비밀번호가 올바르지 않습니다.\" } }")
                                    })
                    })

    })
    @PostMapping("/login")
    ResponseEntity<ResPostLoginDto> login(@RequestBody ReqPostLoginDto dto);

    // 관리자 권한 부여
    @Operation(summary = "관리자 권한 부여", description = "관리자 권한 부여를 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 부여 성공", content = @Content(schema = @Schema(implementation = ResPatchToAdminDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {
                            @Content(mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "권한 부족",
                                                    value = "{\"error\": { \"code\": \"ACCESS_DENIED\", \"message\": \"접근 권한이 없습니다.\" } }"),
                                            @ExampleObject(name = "해당 사용자 없음",
                                                    value = "{\"error\": { \"code\": \"USER_NOT_FOUND\", \"message\": \"사용자를 찾을 수 없습니다.\" } }")
                                    })
                    })

    })
    @PostMapping("/admin/users/{userId}/roles")
    ResponseEntity<ResPatchToAdminDto> toAdmin(@PathVariable("userId") UUID userId, UserDetailsImpl userDetails);
}
