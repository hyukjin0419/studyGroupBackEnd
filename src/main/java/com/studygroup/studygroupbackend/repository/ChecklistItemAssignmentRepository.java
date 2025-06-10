package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.ChecklistItemAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChecklistItemAssignmentRepository extends JpaRepository<ChecklistItemAssignment, Long> {
    Optional<ChecklistItemAssignment> findByChecklistItemIdAndMemberId(Long checklistId, Long memberId);

    List<ChecklistItemAssignment> findAllByMemberId(Long memberId);
    List<ChecklistItemAssignment> findAllByChecklistItem_Study_Id(Long studyId);

    List<ChecklistItemAssignment> findAllByChecklistItem_Study_IdOrderByStudyOrderIndexAsc(Long studyId);
    List<ChecklistItemAssignment> findAllByMemberIdOrderByPersonalOrderIndexAsc(Long memberId);


    @Query("SELECT MAX(cia.studyOrderIndex) FROM ChecklistItemAssignment cia WHERE cia.checklistItem.study.id = :studyId")
    Optional<Integer> findMaxStudyOrderIndex (Long studyId);
    @Query("SELECT MAX(cia.personalOrderIndex) FROM ChecklistItemAssignment cia WHERE cia.checklistItem.study.id = :studyId")
    Optional<Integer> findMaxPersonalOrderIndex (Long studyId);
}
