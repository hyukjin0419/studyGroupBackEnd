package com.studygroup.studygroupbackend.security;

import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 데이터베이스에서 사용자 정보를 로드하는 커스텀 UserDetailsService 구현
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        
        // ROLE_ 접두사는 Spring Security 규칙
        String role = "ROLE_" + member.getRole().name();
        
        return new User(
                member.getUserName(),
                member.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
