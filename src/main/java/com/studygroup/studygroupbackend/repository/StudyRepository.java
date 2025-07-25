package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByJoinCode(String joinCode);
}
