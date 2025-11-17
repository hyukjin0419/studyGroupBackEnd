package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByJoinCode(String joinCode);
    Optional<Study> findByIdAndDeletedFalse(Long studyId);

    @Modifying
    @Query("""
        UPDATE Study s
        Set s.deleted = true, s.deletedAt = CURRENT_TIMESTAMP
        Where s.id IN(
            SELECT sm.study.id
            FROM StudyMember sm
            WHERE sm.member.id = :memberId
            AND sm.studyRole = 'LEADER'
        )
""")
    void softDeleteAllByLeaderMemberId(Long memberId);

    @Modifying
    @Query(
            value = "DELETE FROM studies WHERE deleted = true AND deleted_at <= :threshold",
            nativeQuery = true
    )
    int deleteExpired(LocalDateTime threshold);
}
