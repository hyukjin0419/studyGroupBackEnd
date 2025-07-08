package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrashBinStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pickupZone; // 예: "A-1"

    private double plasticKg;

    private double paperKg;

    private double generalKg;

    private LocalDateTime lastUpdated;
}
