package com.studygroup.studygroupbackend.admin.repository;

import com.studygroup.studygroupbackend.admin.entity.AdminRole;
import com.studygroup.studygroupbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 관리자 권한 정보에 대한 데이터 액세스 인터페이스
 */
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {
    
    /**
     * 특정 멤버의 관리자 권한 정보 조회
     * 
     * @param member 조회할 멤버
     * @return 멤버의 관리자 권한 정보
     */
    Optional<AdminRole> findByMember(Member member);
    
    /**
     * 특정 멤버 ID에 대한 관리자 권한 정보 조회
     * 
     * @param memberId 조회할 멤버 ID
     * @return 멤버의 관리자 권한 정보
     */
    Optional<AdminRole> findByMemberId(Long memberId);
}
