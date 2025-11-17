package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByStudy(Study study);
    @Query("SELECT sm FROM StudyMember sm " +
            "WHERE sm.member.id = :memberId")
    List<StudyMember> findByMember_Id(@Param("memberId") Long memberId);

    @Query("SELECT MAX(sm.personalOrderIndex) FROM StudyMember sm WHERE sm.member.id = :memberId")
    Optional<Integer> findMaxPersonalOrderIndexByMemberId(@Param("memberId") Long memberId);


    @Modifying
    @Query("UPDATE StudyMember sm SET sm.personalOrderIndex = :orderIndex " +
            "WHERE sm.member.id = :memberId AND sm.study.id = :studyId")
    void updatePersonalOrderIndex(@Param("memberId") Long memberId,
                                  @Param("studyId") Long studyId,
                                  @Param("orderIndex") Integer orderIndex);

    //내가 속한 스터디들 + 스터디 정보
    @Query("""
        SELECT sm FROM StudyMember sm
        JOIN FETCH sm.study
        WHERE sm.member.id = :memberId
        ORDER BY sm.personalOrderIndex ASC
""")
    List<StudyMember> findStudyMembersByMemberIdOrderByPersonalOrderIndexAsc(@Param("memberId") Long memberId);

    //특정 스터디들에 속한 모든 멤버 + member 정보
    @Query("""
        SELECT sm FROM StudyMember sm
        JOIN FETCH sm.member
        WHERE sm.study.id IN :studyIds
""")
    List<StudyMember> findStudyMembersByStudyIdIn(@Param("studyIds") List<Long> studyIds);


    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    @Query(
            value = "SELECT * FROM study_members WHERE study_id = :studyId AND member_id = :memberId",
            nativeQuery = true
    )
    Optional<StudyMember>findByStudyIdAndMemberIdIncludingDeleted(Long studyId, Long memberId);

    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);
    Optional<StudyMember> findByStudyIdAndMemberIdAndStudyRole(Long studyId, Long memberId, StudyRole role);

    @Query("SELECT sm.member.id FROM StudyMember sm WHERE sm.study.id = :studyId")
    List<Long> findMemberIdsByStudyId(@Param("studyId") Long studyId);

    List<StudyMember> findByStudyIdAndDeletedFalse(Long studyId);
    Optional<StudyMember> findByIdAndDeletedFalse(Long studyMemberId);

    @Modifying
    @Query("""
        UPDATE StudyMember sm
        SET sm.deleted = true, sm.deletedAt = CURRENT_TIMESTAMP 
        WHERE sm.member.id = :memberId
""")
    void softDeleteAllByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM StudyMember sm WHERE sm.deleted = true AND sm.deletedAt <= :threshold")
    void deleteExpired(LocalDateTime threshold);
}
