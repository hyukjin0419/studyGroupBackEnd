package com.studygroup.studygroupbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    
    /**
     * 사용자 역할 (기본값: 일반 사용자)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    private Member(String userName, String password, String email, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * 기본 사용자(USER 역할) 생성
     */
    public static Member of(String userName, String password, String email) {
        return new Member(userName, password, email, UserRole.USER);
    }
    
    /**
     * 지정된 역할을 가진 사용자 생성
     */
    public static Member of(String userName, String password, String email, UserRole role) {
        return new Member(userName, password, email, role);
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
