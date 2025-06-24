package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
