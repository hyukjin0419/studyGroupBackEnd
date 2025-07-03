package com.studygroup.studygroupbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

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
            .build();
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
