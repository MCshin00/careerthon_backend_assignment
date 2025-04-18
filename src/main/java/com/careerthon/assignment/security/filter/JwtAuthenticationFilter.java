package com.careerthon.assignment.security.filter;

import com.careerthon.assignment.exception.ErrorResponseDto;
import com.careerthon.assignment.exception.JwtExceptionMessage;
import com.careerthon.assignment.security.UserDetailsServiceImpl;
import com.careerthon.assignment.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 인증 필터")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = req.getRequestURI();

        if (FilterExcludePath.shouldExclude(requestUri)) {
            filterChain.doFilter(req, res);
            return;
        }

        String token = jwtUtil.extractToken(req);

        if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
            sendErrorResponse(res, JwtExceptionMessage.INVALID_TOKEN);
            return;
        }

        Claims claims = jwtUtil.parseClaims(token);
        String username = claims.get("username", String.class);
        setAuthentication(username);

        filterChain.doFilter(req, res);
    }

    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void sendErrorResponse(HttpServletResponse res, JwtExceptionMessage jwtError) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ErrorResponseDto response = ErrorResponseDto.of(jwtError);
        res.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }
}


