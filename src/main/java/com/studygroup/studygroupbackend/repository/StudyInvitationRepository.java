package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.StudyInvitation;
import com.studygroup.studygroupbackend.domain.status.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyInvitationRepository extends JpaRepository<StudyInvitation, Long> {
    boolean existsByStudyIdAndInviteeIdAndStatus(
            Long study_id, Long invitee_id, InvitationStatus status
    );
}
