package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.StudyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "studies")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private String color = "0xFF8AB4F8";

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = true)
    private Double progress;

    @Column(nullable = false)
    private StudyStatus status = StudyStatus.PROGRESSING;


    public static Study of(
            String name,
            String description,
            String color,
            LocalDateTime dueDate,
            Double progress,
            StudyStatus status
            ) {
        return Study.builder()
                .name(name)
                .description(description)
                .color(color != null ? color : "0xFF8AB4F8")
                .dueDate(dueDate != null ? dueDate : LocalDateTime.now().plusDays(30))
                .progress(progress != null ? progress : 0.0)
                .status(status != null ? status : StudyStatus.PROGRESSING)
                .build();
    }

    public void updateStudyInfo(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

//    public void changeLeader (Member newLeader) {
//        this.leader = newLeader;
//    }

//    public static Study of(String name, String description, Member leader) {
//        return new Study(name, description, leader);
//    }
}
