package com.studygroup.studygroupbackend.domain;

import com.studygroup.studygroupbackend.domain.status.Role;
import com.studygroup.studygroupbackend.domain.superEntity.BaseEntity;
import com.studygroup.studygroupbackend.domain.superEntity.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@SQLRestriction("deleted = false")
public class Member extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    public static Member of(String userName, String password, String email) {
        return Member.builder()
            .userName(userName)
            .password(password)
            .email(email)
            .role(Role.USER)
            .deleted(false)
            .build();
    }

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @PrePersist
    private void initUuid(){
        if(this.uuid == null){
            this.uuid = UUID.randomUUID().toString();
        }
    }

    /*
    Setter 사용하지 말고 필요하면 명확한 비지니스 메서드를 생성하여 관리
    setPassword() -> x
    changePassword() -> o
     */
    public void updateProfile(String userName,String email) {
        this.userName = userName;
        this.email = email;
    }
}
