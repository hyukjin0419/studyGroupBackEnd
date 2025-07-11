package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.StudyRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByStudy(Study study);
    List<StudyMember> findByMemberId(Long memberId);
    List<StudyMember> findByMemberIdOrderByPersonalOrderIndexAsc(Long memberId);
    void deleteByStudy(Study study);

    @Query("SELECT MAX(sm.personalOrderIndex) FROM StudyMember sm WHERE sm.member.id = :memberId")
    Optional<Integer> findMaxPersonalOrderIndexByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE StudyMember sm SET sm.personalOrderIndex = :orderIndex " +
            "WHERE sm.member.id = :memberId AND sm.study.id = :studyId")
    void updatePersonalOrderIndex(@Param("memberId") Long memberId,
                                  @Param("studyId") Long studyId,
                                  @Param("orderIndex") Integer orderIndex);


    boolean existsByStudyAndMember(Study study, Member member);
    Optional<StudyMember> findByStudyAndMember(Study study, Member member);
    Optional<StudyMember> findByStudyIdAndMemberIdAndStudyRole(Long studyId, Long memberId, StudyRole role);

}
