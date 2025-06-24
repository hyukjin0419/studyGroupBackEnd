package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.StudyRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByStudy(Study study);
    List<StudyMember> findByMemberId(Long memberId);

    void deleteByStudy(Study study);

    boolean existsByStudyAndMember(Study study, Member member);
    Optional<StudyMember> findByStudyAndMember(Study study, Member member);
    Optional<StudyMember> findByStudyIdAndMemberIdAndStudyRole(Long studyId, Long memberId, StudyRole role);

}
