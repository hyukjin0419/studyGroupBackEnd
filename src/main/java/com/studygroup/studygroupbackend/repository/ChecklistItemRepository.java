package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.ChecklistItem;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
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
    List<ChecklistItem> findByStudyIdInAndTargetDateBetween(List<Long> studyIds, LocalDate startDate, LocalDate endDate);
}
