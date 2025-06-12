package com.studygroup.studygroupbackend.security.repository;

import com.studygroup.studygroupbackend.security.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
