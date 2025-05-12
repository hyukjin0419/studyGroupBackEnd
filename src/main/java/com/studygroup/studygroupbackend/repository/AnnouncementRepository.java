package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByPublishDateBeforeAndExpiryDateAfterOrExpiryDateIsNullOrderByImportantDescPublishDateDesc(
            LocalDateTime now, LocalDateTime now2);
}
