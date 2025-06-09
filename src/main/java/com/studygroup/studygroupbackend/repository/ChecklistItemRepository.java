package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

}
