package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "checklist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id", nullable = true)
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private boolean isChecked;

    private LocalDateTime checkedAt;
}
