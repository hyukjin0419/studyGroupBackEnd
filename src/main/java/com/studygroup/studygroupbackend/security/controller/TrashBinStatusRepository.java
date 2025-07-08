package com.studygroup.studygroupbackend.security.controller;

import com.example.demo.domain.TrashBinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrashBinStatusRepository extends JpaRepository<TrashBinStatus, Long> {
    Optional<TrashBinStatus> findByPickupZone(String pickupZone);
}
