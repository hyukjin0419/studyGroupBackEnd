package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

}
