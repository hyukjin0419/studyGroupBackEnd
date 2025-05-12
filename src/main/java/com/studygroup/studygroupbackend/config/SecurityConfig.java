package com.studygroup.studygroupbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 설정 클래스
 * 관리자 인증 및 권한 설정을 담당합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 비밀번호 인코더 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 보안 필터 체인 설정
     * URL 패턴별 접근 권한 및 로그인/로그아웃 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // 정적 리소스에 대한 접근 허용 (관리자 페이지 리소스 포함)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/admin/js/**", "/admin/css/**", "/admin/images/**", "/webjars/**", "/favicon.ico").permitAll()
                // API CSRF 토큰 가져오기 엔드포인트 허용
                .requestMatchers("/api/admin/get-csrf", "/api/admin/current-user").permitAll()
                // 로그인 페이지 접근 허용
                .requestMatchers("/login", "/login/**").permitAll()
                // 관리자 페이지 허용 (HTML 페이지)
                .requestMatchers("/admin").hasRole("ADMIN")
                // 관리자 API 허용 (비관리자 허용 경로 제외)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // 그 외 경로는 모두 허용
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .csrf((csrf) -> csrf
                // REST API 호출은 CSRF 보호 변경
                .ignoringRequestMatchers("/api/admin/**")
            )
            // 세션 관리 설정
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?invalid-session")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired-session")
            );
        
        return http.build();
    }
    
    /**
     * 실제로는 CustomUserDetailsService가 필요해지지 않지만,
     * Spring Security에서는 이 빈이 먼저 임포트되기 때문에 명시적으로 노출하지 않아도 됩니다.
     * CustomUserDetailsService가 @Service로 등록되어 자동으로 사용됩니다.
     */
}
