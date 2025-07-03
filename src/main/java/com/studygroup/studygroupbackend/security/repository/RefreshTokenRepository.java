package com.studygroup.studygroupbackend.security.repository;

import com.studygroup.studygroupbackend.security.jwt.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(Long memberId);
    //여기 List로 안 받아 오면 중복 로그인 불가한가?
    Optional<RefreshToken> findByToken(String token);
    void deleteByMemberId(Long memberId);

}
