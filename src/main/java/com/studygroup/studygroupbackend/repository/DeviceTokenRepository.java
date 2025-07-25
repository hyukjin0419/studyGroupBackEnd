package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.DeviceToken;
import com.studygroup.studygroupbackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    boolean existsByMemberIdAndFcmToken(Long memberId, String fcmToken);
}
