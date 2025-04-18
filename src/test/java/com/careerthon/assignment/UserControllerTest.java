package com.careerthon.assignment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.careerthon.assignment.domain.dtos.request.ReqPostLoginDto;
import com.careerthon.assignment.domain.dtos.request.ReqPostSignUpDto;
import com.careerthon.assignment.model.entity.User;
import com.careerthon.assignment.model.entity.UserRole;
import com.careerthon.assignment.model.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ReqPostSignUpDto validSignupDto;
    private ReqPostLoginDto validLoginDto;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        passwordEncoder = new BCryptPasswordEncoder();

        validSignupDto = ReqPostSignUpDto.builder()
                .username("user1")
                .password("password123")
                .nickname("유저1")
                .build();

        validLoginDto = ReqPostLoginDto.builder()
                .username("user1")
                .password("password123")
                .build();

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .nickname("admin")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);

        adminToken = obtainToken("admin", "admin123");
    }

    private String obtainToken(String username, String password) throws Exception {
        String content = objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", password
        ));

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTests {

        @Test
        @DisplayName("정상 회원가입")
        void testSignupSuccess() throws Exception {
            mockMvc.perform(post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validSignupDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("user1"))
                    .andExpect(jsonPath("$.nickname").value("유저1"))
                    .andExpect(jsonPath("$.role").value("USER"));
        }

        @Test
        @DisplayName("중복 회원가입 시도")
        void testSignupDuplicate() throws Exception {
            mockMvc.perform(post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validSignupDto)))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validSignupDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
                    .andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTests {

        @BeforeEach
        void createUser() throws Exception {
            mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSignupDto)));
        }

        @Test
        @DisplayName("정상 로그인")
        void testLoginSuccess() throws Exception {
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists());
        }

        @Test
        @DisplayName("비밀번호가 틀렸을 때 로그인 실패")
        void testLoginWrongPassword() throws Exception {
            ReqPostLoginDto wrongDto = ReqPostLoginDto.builder()
                    .username("user1")
                    .password("wrongpass")
                    .build();

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(wrongDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
                    .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    @Nested
    @DisplayName("관리자 권한 부여 테스트")
    class ToAdminTests {

        private UUID userId;
        private String userToken;

        @BeforeEach
        void createUser() throws Exception {
            mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validSignupDto)));

            userId = userRepository.findByUsername("user1").get().getId();
            userToken = obtainToken("user1", "password123");
        }

        @Test
        @DisplayName("관리자 권한이 있는 사용자가 권한 부여")
        void testToAdminSuccess() throws Exception {
            mockMvc.perform(patch("/admin/users/" + userId + "/roles")
                            .header(HttpHeaders.AUTHORIZATION, adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.role").value("ADMIN"));
        }

        @Test
        @DisplayName("일반 사용자가 권한 부여 시도")
        void testToAdminFailByUser() throws Exception {
            mockMvc.perform(patch("/admin/users/" + userId + "/roles")
                            .header(HttpHeaders.AUTHORIZATION, userToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
                    .andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
        }

        @Test
        @DisplayName("존재하지 않는 사용자에게 권한 부여 시도")
        void testToAdminFailByInvalidId() throws Exception {
            UUID fakeId = UUID.randomUUID();

            mockMvc.perform(patch("/admin/users/" + fakeId + "/roles")
                            .header(HttpHeaders.AUTHORIZATION, adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
                    .andExpect(jsonPath("$.error.message").value("사용자를 찾을 수 없습니다."));
        }
    }
}
