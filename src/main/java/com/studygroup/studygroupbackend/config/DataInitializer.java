package com.studygroup.studygroupbackend.config;

import com.studygroup.studygroupbackend.admin.entity.AdminRole;
import com.studygroup.studygroupbackend.admin.repository.AdminRoleRepository;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.UserRole;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            MemberRepository memberRepository, 
            StudyRepository studyRepository, 
            AdminRoleRepository adminRoleRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // 기존 데이터가 있는지 확인
            if (memberRepository.count() == 0) {
                // 관리자 계정 생성
                Member adminMember = Member.of(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "admin@studygroup.com",
                        UserRole.ADMIN // 관리자 역할 지정
                );
                adminMember = memberRepository.save(adminMember);
                
                // 관리자 권한 생성 및 저장
                AdminRole adminRole = AdminRole.ofFullAccess(adminMember);
                adminRoleRepository.save(adminRole);
                
                // 테스트용 일반 사용자 생성
                Member user1 = Member.of(
                        "user1",
                        passwordEncoder.encode("user123"),
                        "user1@studygroup.com"
                        // 기본 역할: USER
                );
                memberRepository.save(user1);
                
                Member user2 = Member.of(
                        "user2",
                        passwordEncoder.encode("user123"),
                        "user2@studygroup.com"
                );
                memberRepository.save(user2);
                
                // 테스트용 스터디 그룹 생성
                Study study1 = Study.of(
                        "자바 스터디",
                        "자바 프로그래밍을 함께 공부하는 스터디 그룹입니다. Spring Boot, JPA, 디자인 패턴 등을 다룹니다.",
                        adminMember
                );
                studyRepository.save(study1);
                
                Study study2 = Study.of(
                        "알고리즘 스터디",
                        "코딩 테스트와 알고리즘 문제 해결을 연습하는 스터디 그룹입니다. 매주 문제를 풀고 리뷰합니다.",
                        user1
                );
                studyRepository.save(study2);
                
                Study study3 = Study.of(
                        "웹 개발 스터디",
                        "HTML, CSS, JavaScript, React 등을 활용한 웹 개발을 공부하는 스터디 그룹입니다.",
                        user2
                );
                studyRepository.save(study3);
                
                System.out.println("초기 데이터 생성 완료");
            }
        };
    }
}
