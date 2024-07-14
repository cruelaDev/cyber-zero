package org.example.cyberzero.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.cyberzero.user.dto.UserResponseDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        Claims claims = jwtService.extract(token);
        if (claims == null) {
            logger.error("JWT claims extraction failed");
            filterChain.doFilter(request, response);
            return;
        }

        String username = claims.getSubject();
        String userJson = claims.get("user", String.class);

        if (StringUtils.isBlank(userJson)) {
            logger.error("User JSON in JWT claims is null or empty");
            filterChain.doFilter(request, response);
            return;
        }

        UserResponseDto userResponseDto;
        try {
            userResponseDto = objectMapper.readValue(userJson, UserResponseDto.class);
        } catch (Exception e) {
            logger.error("Failed to deserialize UserResponseDto from JWT claims", e);
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication;
        if (userResponseDto == null) {
            authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
        } else {
            authentication = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority(userResponseDto.getRole().name())));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
