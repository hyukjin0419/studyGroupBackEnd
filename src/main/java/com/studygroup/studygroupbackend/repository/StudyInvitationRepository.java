package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.StudyInvitation;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyInvitationRepository extends JpaRepository<StudyInvitation, Long> {
    boolean existsByStudyIdAndInviteeIdAndStatus(
            Long study_id, Long invitee_id, InvitationStatus status
    );

    @Query("SELECT si.invitee.id FROM StudyInvitation si WHERE si.study.id = :studyId AND si.status = :status")
    List<Long> findInviteeIdsByStudyIdAndStatus(@Param("studyId") Long studyId, @Param("status") InvitationStatus status);

    List<StudyInvitation> findByStudyIdAndDeletedFalse(Long studyId);

    @Modifying
    @Query(
            value = "DELETE FROM study_invitations WHERE deleted = true AND deleted_at <= :threshold",
            nativeQuery = true
    )
    int deleteExpired(LocalDateTime threshold);

}
