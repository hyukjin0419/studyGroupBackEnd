package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "studies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "leader_id",nullable = false)
//    private Member leader;

//    private Study(String name, String description, Member leader) {
//        this.name = name;
//        this.description = description;
//        this.leader = leader;
//    }

    private Study(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateStudyInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }

//    public void changeLeader (Member newLeader) {
//        this.leader = newLeader;
//    }

//    public static Study of(String name, String description, Member leader) {
//        return new Study(name, description, leader);
//    }
    public static Study of(String name, String description) {
        return new Study(name, description);
    }

}
