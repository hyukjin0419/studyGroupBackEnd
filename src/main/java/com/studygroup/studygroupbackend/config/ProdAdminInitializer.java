package com.studygroup.studygroupbackend.config;

import com.studygroup.studygroupbackend.admin.entity.AdminRole;
import com.studygroup.studygroupbackend.admin.repository.AdminRoleRepository;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.UserRole;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 운영 환경에서 최초 실행 시 관리자 계정을 1회만 생성하는 구성 클래스
 */
@Configuration
@Profile("prod")  // ⚠️ 이 설정이 있어야 운영 환경에서만 작동함
public class ProdAdminInitializer {

    /**
     * 운영 환경용 관리자 계정 1회성 생성
     */
    @Bean
    public CommandLineRunner initProdAdmin(
            MemberRepository memberRepository,
            AdminRoleRepository adminRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String adminEmail = "admin@studygroup.com";

            // 기존 관리자 계정이 있는지 확인
            if (memberRepository.findByEmail(adminEmail).isPresent()) {
                System.out.println("[ProdAdminInitializer] 관리자 계정이 이미 존재합니다. 생성을 건너뜁니다.");
                return;
            }

            System.out.println("[ProdAdminInitializer] 관리자 계정이 존재하지 않아 생성을 시작합니다...");

            // 1. 관리자 계정 생성
            Member adminMember = Member.of(
                    "admin",
                    passwordEncoder.encode("SuperSecure123!"),  // ⚠️ 기본 비밀번호, 배포 후 반드시 변경 요망
                    adminEmail,
                    UserRole.ADMIN
            );
            adminMember = memberRepository.save(adminMember);

            // 2. 관리자 권한 생성 및 저장 - 모든 권한 부여
            AdminRole adminRole = AdminRole.ofFullAccess(adminMember);
            adminRoleRepository.save(adminRole);

            System.out.println("[ProdAdminInitializer] 관리자 계정 생성 완료!");
        };
    }
}
