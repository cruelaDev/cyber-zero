package org.example.cyberzero.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.cyberzero.user.dto.UserResponseDto;
import org.example.cyberzero.user.dto.UserTokenResponseDto;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final ObjectMapper objectMapper;
    // todo extract to yaml and fix the number
    private String secret = "l0pNlSGI6AkrljL27C7dq562oRSUksVezVXonzCPL/2ID1bspT3MBQIds2pE3zuB";
    private Integer expiration = 5 * 60 * 60 * 60;
    private Integer refresh = 31 * 24 * 60 * 60 * 60;

    public UserTokenResponseDto generate(UserResponseDto userResponseDto) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        String accessToken;
        String refreshToken;
        try {
            accessToken = Jwts.builder()
                    .setSubject(userResponseDto.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .claim("user", objectMapper.writeValueAsString(userResponseDto))
                    .signWith(secretKey)
                    .compact();

            refreshToken = Jwts.builder()
                    .setSubject(userResponseDto.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + refresh))
                    .claim("user", objectMapper.writeValueAsString(userResponseDto))
                    .signWith(secretKey)
                    .compact();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new UserTokenResponseDto(accessToken, refreshToken);
    }

    public Claims extract(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
