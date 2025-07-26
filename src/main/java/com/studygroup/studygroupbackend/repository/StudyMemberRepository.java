package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.status.StudyRole;
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


    boolean existsByStudyAndMember(Study study, Member member);

    boolean existsByStudyIdAndMemberId(Long studyId, Long memberId);

    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);
    Optional<StudyMember> findByStudyIdAndMemberIdAndStudyRole(Long studyId, Long memberId, StudyRole role);


}
