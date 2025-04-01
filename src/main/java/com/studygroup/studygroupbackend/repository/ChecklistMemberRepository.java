package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.Checklist;
import com.studygroup.studygroupbackend.entity.ChecklistMember;
import com.studygroup.studygroupbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistMemberRepository extends JpaRepository<ChecklistMember, Long> {
    Optional<ChecklistMember> findByChecklistIdAndMemberId(Long checklistId, Long memberId);

    List<ChecklistMember> findAllByMemberId(Long memberId);
    List<ChecklistMember> findAllByChecklist_Study_Id(Long studyId);




}
