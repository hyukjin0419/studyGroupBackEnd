package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.ChecklistItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

    @Query("""
        SELECT ci FROM ChecklistItem ci
        WHERE ci.study.id = :studyId
          AND ci.targetDate = :targetDate
          AND ci.deleted = false
        ORDER BY ci.orderIndex
    """)
    List<ChecklistItem> findByStudyIdAndTargetDate(
            @Param("studyId") Long studyId,
            @Param("targetDate") LocalDate targetDate
    );

    List<ChecklistItem> findByStudyIdAndTargetDateBetween(Long studyId, LocalDate startDate, LocalDate endDate);
    List<ChecklistItem> findAllByStudyMember_Member_IdAndTargetDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query(value = """
    UPDATE checklist_items ci
    SET ci.deleted = true, ci.deleted_at = CURRENT_TIMESTAMP
    WHERE ci.study_member_id IN (
        SELECT id FROM study_members 
        WHERE member_id = :memberId AND deleted = false
    )
    AND ci.deleted = false
    """, nativeQuery = true)
    void softDeleteAllByMemberId(@Param("memberId") Long memberId);

    List<ChecklistItem> findByStudyIdAndDeletedFalse(Long studyId);
//
//    @Modifying
//    @Query("""
//        UPDATE ChecklistItem ci
//        SET ci.deleted = true
//        WHERE ci.studyMember.id = :studyMemberId
//        AND ci.deleted = false
//""")
//    void softDeleteByStudyMemberId(Long studyMemberId);

    @Modifying
    @Query("""
        UPDATE ChecklistItem ci
        SET ci.deleted = true, ci.deletedAt = CURRENT_TIMESTAMP
        WHERE ci.studyMember.id = :studyMemberId
        AND ci.deleted = false
    """)
    void softDeleteByStudyMemberId(@Param("studyMemberId") Long studyMemberId);

    @Modifying
    @Query(value = "DELETE FROM checklist_items WHERE deleted = true AND deleted_at <= :threshold", nativeQuery = true)
    int deleteExpired(LocalDateTime threshold);
}
