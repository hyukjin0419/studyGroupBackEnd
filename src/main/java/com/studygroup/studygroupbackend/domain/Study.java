package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.*;

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

    public static Study of(String name, String description, String color) {
        return Study.builder()
                .name(name)
                .description(description)
                .color(color)
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
