package com.studygroup.studygroupbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                // 관리자 페이지 및 API는 인증 필요
                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                // 그 외 경로는 모두 허용
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard.html", true)
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
                .ignoringRequestMatchers(
                    "/api/admin/**"
                ));
        
        return http.build();
    }
    
    /**
     * 실제로는 CustomUserDetailsService가 필요해지지 않지만,
     * Spring Security에서는 이 빈이 먼저 임포트되기 때문에 명시적으로 노출하지 않아도 됩니다.
     * CustomUserDetailsService가 @Service로 등록되어 자동으로 사용됩니다.
     */
}
