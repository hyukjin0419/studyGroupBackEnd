package com.studygroup.studygroupbackend.security.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long memberId;
    private final String userName;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomUserDetails(Long memberId, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.userName = userName;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails ofLogin(Long memberId, String userName, String password, String role) {
        return new CustomUserDetails(
                memberId,
                userName,
                password,
                Collections.singleton(() -> "ROLE_" + role)
        );
    }

    public static CustomUserDetails ofJwt(Long memberId, String userName, String role) {
        return new CustomUserDetails(
                memberId,
                userName,
                null,
                Collections.singleton(() -> "ROLE_" + role)
        );
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return userName; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
