package com.studygroup.studygroupbackend.config;

import com.studygroup.studygroupbackend.admin.entity.AdminRole;
import com.studygroup.studygroupbackend.admin.repository.AdminRoleRepository;
import com.studygroup.studygroupbackend.entity.Announcement;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.UserRole;
import com.studygroup.studygroupbackend.repository.AnnouncementRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;


/**
 * 애플리케이션 초기 데이터를 생성하는 구성 클래스
 * 최초 실행 시 기본 사용자, 관리자, 스터디 그룹, 공지사항 등을 생성
 */
@Configuration
public class DevDataInitializer {

    /**
     * 개발 및 테스트 환경용 초기 데이터 생성
     */
    @Bean
    @Profile({"dev", "default"})
    public CommandLineRunner initData(
            MemberRepository memberRepository, 
            StudyRepository studyRepository, 
            AdminRoleRepository adminRoleRepository,
            AnnouncementRepository announcementRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // 기존 데이터가 있는지 확인
            if (memberRepository.count() == 0) {
                System.out.println("초기 데이터 생성 시작...");
                
                // 1. 관리자 계정 생성
                Member adminMember = Member.of(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "admin@studygroup.com",
                        UserRole.ADMIN // 관리자 역할 지정
                );
                adminMember = memberRepository.save(adminMember);
                
                // 관리자 권한 생성 및 저장 - 모든 권한 부여
                AdminRole adminRole = AdminRole.ofFullAccess(adminMember);
                adminRoleRepository.save(adminRole);
                
                
                // 3. 테스트용 일반 사용자 생성
                Member user1 = Member.of(
                        "user1",
                        passwordEncoder.encode("user123"),
                        "user1@studygroup.com"
                );
                memberRepository.save(user1);
                
                Member user2 = Member.of(
                        "user2",
                        passwordEncoder.encode("user123"),
                        "user2@studygroup.com"
                );
                memberRepository.save(user2);
                
                // 다양한 사용자 추가 (비밀번호 초기화 테스트용)
                for (int i = 3; i <= 10; i++) {
                    Member user = Member.of(
                            "user" + i,
                            passwordEncoder.encode("user123"),
                            "user" + i + "@studygroup.com"
                    );
                    memberRepository.save(user);
                }
                
                // 4. 테스트용 스터디 그룹 생성
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
                
                // 여러 사용자가 리더인 스터디 더 추가 (회원 삭제 테스트용)
                for (int i = 3; i <= 7; i++) {
                    Member user = memberRepository.findByUserName("user" + i).orElse(null);
                    if (user != null) {
                        Study study = Study.of(
                                "테스트 스터디 " + i,
                                "테스트 스터디 그룹 " + i + "입니다.",
                                user
                        );
                        studyRepository.save(study);
                    }
                }
                
                // 5. 공지사항 생성
                // 관리자 계정을 작성자로 설정
                Announcement announcement1 = Announcement.of(
                        "스터디 그룹 서비스 오픈 안내",
                        "안녕하세요, 스터디 그룹 서비스가 정식 오픈되었습니다. 많은 이용 부탁드립니다!",
                        adminMember, // 관리자가 작성
                        LocalDateTime.now().minusDays(7), // 게시일
                        null, // 만료일 없음
                        true  // 중요 공지
                );
                announcementRepository.save(announcement1);
                
                Announcement announcement2 = Announcement.of(
                        "시스템 점검 안내",
                        "5월 15일 오전 2시부터 4시까지 시스템 점검이 예정되어 있습니다. 이용에 참고 부탁드립니다.",
                        adminMember, 
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().plusDays(5), // 만료일 
                        false // 일반 공지
                );
                announcementRepository.save(announcement2);
                
                Announcement announcement3 = Announcement.of(
                        "비밀번호 변경 권장 안내",
                        "보안 강화를 위해 주기적인 비밀번호 변경을 권장드립니다.",
                        adminMember,
                        LocalDateTime.now(),
                        null, // 만료일 없음
                        false // 일반 공지
                );
                announcementRepository.save(announcement3);
                
                System.out.println("초기 데이터 생성 완료!");
                System.out.println("총 생성된 계정: " + memberRepository.count() + "개");
                System.out.println("총 생성된 스터디: " + studyRepository.count() + "개");
                System.out.println("총 생성된 공지사항: " + announcementRepository.count() + "개");
            } else {
                System.out.println("이미 데이터가 존재합니다. 초기 데이터 생성을 건너뜁니다.");
            }
        };
    }
}
