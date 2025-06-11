package com.studygroup.studygroupbackend.jwt;

import com.studygroup.studygroupbackend.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key secreteKey;

    private final long accessTokenValidTime = 1000L * 60 * 30; //30분
    private final long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7;//7일

    @PostConstruct
    protected void init() {
        this.secreteKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        System.out.println("Loaded JWT_SECRET: " + secretKeyString.substring(0, 8) + "...");
    }

    public String generateAccessToken(String userName, Role role) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role",role.name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .signWith(secreteKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public String generateRefreshToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .signWith(secreteKey, SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secreteKey).build().parseClaimsJwt(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserName (String token) {
        return Jwts.parserBuilder().setSigningKey(secreteKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secreteKey).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public Authentication getAuthentication(String token) {
        String username = getUserName(token);
        String role = getRole(token);
        User userDetails = new User(username, "", List.of(() -> "ROLE_"+role));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
