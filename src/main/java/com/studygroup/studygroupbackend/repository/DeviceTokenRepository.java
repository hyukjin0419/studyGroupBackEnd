package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.DeviceToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    boolean existsByMemberIdAndFcmToken(Long memberId, String fcmToken);

    @Query("SELECT d.fcmToken FROM DeviceToken d WHERE d.member.id = :memberId")
    List<String> findFcmTokensByMemberId(@Param("memberId") Long memberId);

    void deleteByFcmTokenAndMemberIdNot(String fcmToken, Long memberId);
    void deleteByMemberIdAndFcmToken(Long memberId, String deviceToken);
}
