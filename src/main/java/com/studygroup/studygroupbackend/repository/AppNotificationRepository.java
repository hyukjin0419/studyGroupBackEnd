package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {
}
