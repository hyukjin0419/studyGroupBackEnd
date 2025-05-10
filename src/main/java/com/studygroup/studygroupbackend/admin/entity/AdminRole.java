package com.studygroup.studygroupbackend.admin.entity;

import com.studygroup.studygroupbackend.entity.BaseEntity;
import com.studygroup.studygroupbackend.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 관리자 권한 정보를 저장하는 엔티티
 * Member 엔티티와 1:1 관계를 가지며, 해당 멤버가 가진 관리자 권한 목록을 관리합니다.
 */
@Entity
@Table(name = "admin_roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 관리자 권한을 가진 멤버 (1:1 관계)
     */
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    /**
     * 관리자가 가진, AdminPermission 열거형으로 정의된 권한 목록
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "admin_permissions",
        joinColumns = @JoinColumn(name = "admin_role_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private Set<AdminPermission> permissions = new HashSet<>();

    /**
     * 새 AdminRole 인스턴스 생성
     */
    private AdminRole(Member member, Set<AdminPermission> permissions) {
        this.member = member;
        this.permissions = permissions;
    }

    /**
     * 지정된 멤버와 권한 목록으로 AdminRole 생성
     */
    public static AdminRole of(Member member, Set<AdminPermission> permissions) {
        return new AdminRole(member, permissions);
    }
    
    /**
     * 모든 관리자 권한을 가진 AdminRole 생성
     */
    public static AdminRole ofFullAccess(Member member) {
        Set<AdminPermission> allPermissions = Set.of(
            AdminPermission.VIEW_ALL_MEMBERS,
            AdminPermission.FORCE_DELETE_STUDY,
            AdminPermission.MANAGE_ANNOUNCEMENTS,
            AdminPermission.MANAGE_USER_ROLES,
            AdminPermission.VIEW_SYSTEM_LOGS
        );
        return new AdminRole(member, allPermissions);
    }
    
    /**
     * 특정 권한 보유 여부 확인
     */
    public boolean hasPermission(AdminPermission permission) {
        return permissions.contains(permission);
    }
    
    /**
     * 권한 추가
     */
    public void addPermission(AdminPermission permission) {
        permissions.add(permission);
    }
    
    /**
     * 권한 제거
     */
    public void removePermission(AdminPermission permission) {
        permissions.remove(permission);
    }
}
