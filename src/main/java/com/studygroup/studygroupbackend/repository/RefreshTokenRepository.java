package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserName(String userName);
    void deleteByUserName(String userName);
}
