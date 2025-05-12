package com.studygroup.studygroupbackend.admin.service.impl;

import com.studygroup.studygroupbackend.admin.dto.AdminDto;
import com.studygroup.studygroupbackend.admin.entity.AdminPermission;
import com.studygroup.studygroupbackend.admin.entity.AdminRole;
import com.studygroup.studygroupbackend.admin.repository.AdminRoleRepository;
import com.studygroup.studygroupbackend.admin.service.AdminService;
import com.studygroup.studygroupbackend.entity.Announcement;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import com.studygroup.studygroupbackend.entity.UserRole;
import com.studygroup.studygroupbackend.repository.AnnouncementRepository;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the AdminService interface.
 * Provides administrative operations with appropriate permission checks.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final AnnouncementRepository announcementRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 관리자의 권한 목록을 조회하는 메서드
     * 데이터베이스에서 관리자의 권한 정보를 조회합니다.
     *
     * @param adminId 관리자 ID
     * @return 권한 목록, 권한이 없으면 빈 Set 반환
     */
    private Set<AdminPermission> getAdminPermissions(Long adminId) {
        return adminRoleRepository.findByMember_Id(adminId)
                .map(AdminRole::getPermissions)
                .orElse(Collections.emptySet());
    }
    
    /**
     * 관리자가 해당 권한을 가지고 있는지 확인하는 메서드
     * 권한이 없을 경우 403 Forbidden 예외를 던짐
     *
     * @param adminId 관리자 ID
     * @param requiredPermission 요구되는 권한
     * @throws ResponseStatusException 권한 없음 (HTTP 403) 또는 관리자 찾을 수 없음 (HTTP 404) 또는 관리자가 아님 (HTTP 403)
     */
    private Member checkPermission(Long adminId, AdminPermission requiredPermission) {
        // 1. 멤버 존재 여부 확인
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "멤버를 찾을 수 없습니다: ID " + adminId
                ));
        
        // 2. 관리자 역할 여부 확인
        if (admin.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "관리자 권한이 없는 사용자입니다: " + adminId
            );
        }
        
        // 3. 특정 관리자 권한 보유 여부 확인
        Set<AdminPermission> permissions = getAdminPermissions(adminId);
        if (!permissions.contains(requiredPermission)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, 
                    "해당 작업을 수행할 권한이 없습니다: " + requiredPermission
            );
        }
        
        return admin;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminDto.MemberResponse> getAllMembers(Long adminId) {
        // VIEW_ALL_MEMBERS 권한 체크
        checkPermission(adminId, AdminPermission.VIEW_ALL_MEMBERS);
        
        // 모든 사용자 조회 및 DTO 변환
        return memberRepository.findAll().stream()
                .map(AdminDto.MemberResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminDto.StudyResponse> getAllStudyGroups(Long adminId) {
        // 관리자 권한 체크 - 모든 관리자는 스터디 조회 가능
        // 별도 권한 필요 없음, 관리자 존재 여부만 확인
        memberRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다: ID " + adminId));
        
        // 모든 스터디 그룹 조회 및 DTO 변환
        return studyRepository.findAll().stream()
                .map(AdminDto.StudyResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void forceDeleteStudy(Long adminId, Long studyId) {
        // FORCE_DELETE_STUDY 권한 체크
        checkPermission(adminId, AdminPermission.FORCE_DELETE_STUDY);
        
        // 스터디 존재 여부 확인
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다: ID " + studyId));
        
        // 스터디 삭제
        studyRepository.delete(study);
    }

    @Override
    @Transactional
    public AdminDto.AnnouncementResponse createAnnouncement(Long adminId, AdminDto.AnnouncementRequest request) {
        // MANAGE_ANNOUNCEMENTS 권한 체크 및 관리자 조회
        Member admin = checkPermission(adminId, AdminPermission.MANAGE_ANNOUNCEMENTS);
        
        // 공지사항 생성 및 저장
        LocalDateTime publishDate = request.getPublishDate();
        if (publishDate == null) {
            // publishDate가 없으면 현재 시간으로 기본값 설정
            publishDate = LocalDateTime.now();
        }
        
        Announcement announcement = Announcement.of(
                request.getTitle(),
                request.getContent(),
                admin,
                publishDate, // 요청에서 받은 게시일 사용 (임시저장 또는 게시 상태 구분)
                request.getExpiryDate(),
                request.isImportant()
        );
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        
        // 생성된 공지사항 DTO로 반환
        return AdminDto.AnnouncementResponse.from(savedAnnouncement);
    }

    @Override
    @Transactional
    public AdminDto.AnnouncementResponse updateAnnouncement(Long adminId, Long announcementId, AdminDto.AnnouncementRequest request) {
        // MANAGE_ANNOUNCEMENTS 권한 체크
        checkPermission(adminId, AdminPermission.MANAGE_ANNOUNCEMENTS);
        
        // 공지사항 조회
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다: ID " + announcementId));
        
        // 공지사항 업데이트
        announcement.update(
                request.getTitle(),
                request.getContent(),
                request.getPublishDate(), // publishDate 추가
                request.getExpiryDate(),
                request.isImportant()
        );
        
        Announcement updatedAnnouncement = announcementRepository.save(announcement);
        
        // 업데이트된 공지사항 DTO로 반환
        return AdminDto.AnnouncementResponse.from(updatedAnnouncement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long adminId, Long announcementId) {
        // MANAGE_ANNOUNCEMENTS 권한 체크
        checkPermission(adminId, AdminPermission.MANAGE_ANNOUNCEMENTS);
        
        // 공지사항 존재 여부 확인
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다: ID " + announcementId));
        
        // 공지사항 삭제
        announcementRepository.delete(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminDto.AnnouncementResponse> getAllAnnouncements(Long adminId) {
        // MANAGE_ANNOUNCEMENTS 권한 체크
        checkPermission(adminId, AdminPermission.MANAGE_ANNOUNCEMENTS);
        
        // 모든 공지사항 조회 및 DTO 변환
        return announcementRepository.findAll().stream()
                .map(AdminDto.AnnouncementResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AdminDto.MemberResponse createMember(Long adminId, AdminDto.MemberRequest request) {
        // 관리자 권한 체크 (모든 관리자는 회원 추가 가능)
        // 별도 권한 필요 없음, 관리자 존재 여부만 확인
        memberRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다: ID " + adminId));
        
        // 사용자명 중복 확인
        if (memberRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 사용자명입니다: " + request.getUserName());
        }
        
        // 이메일 중복 확인
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        
        // 새 회원 생성 - 기본 비밀번호는 "초기비밀번호" 로 설정
        Member newMember = Member.of(
                request.getUserName(),
                "initialPassword", // 기본 비밀번호, 실제로는 랜덤 번호나 비밀번호 암호화 필요
                request.getEmail(),
                UserRole.USER // 기본 사용자 역할로 설정
        );
        
        // 새 회원 저장 및 DTO 반환
        Member savedMember = memberRepository.save(newMember);
        return AdminDto.MemberResponse.from(savedMember);
    }
    
    @Override
    @Transactional
    public AdminDto.MemberResponse updateMember(Long adminId, Long memberId, AdminDto.MemberRequest request) {
        // 관리자 권한 체크 (모든 관리자는 회원 수정 가능)
        // 별도 권한 필요 없음, 관리자 존재 여부만 확인
        memberRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다: ID " + adminId));
        
        // 수정할 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다: ID " + memberId));
        
        // 회원 정보 업데이트
        member.updateProfile(request.getUserName(), request.getEmail());
        
        // 변경사항 저장 및 DTO 반환
        Member updatedMember = memberRepository.save(member);
        return AdminDto.MemberResponse.from(updatedMember);
    }
    
    @Override
    @Transactional
    public void forceDeleteMember(Long adminId, Long memberId) {
        // FORCE_DELETE_MEMBER 권한 체크
        checkPermission(adminId, AdminPermission.FORCE_DELETE_MEMBER);
        
        // 대상 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다: ID " + memberId));
        
        // 관리자 자신을 삭제하려는 경우 방지
        if (memberId.equals(adminId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "관리자 자신을 삭제할 수 없습니다.");
        }
        
        // 이 회원이 리더인 모든 스터디 조회
        List<Study> ledStudies = studyRepository.findByLeader_Id(memberId);
        
        if (!ledStudies.isEmpty()) {
            // 리더로 있는 스터디가 있는 경우, 스터디를 먼저 삭제
            for (Study study : ledStudies) {
                studyRepository.delete(study);
            }
        }
        
        // 회원에 연관된 관리자 역할 삭제 (존재하는 경우)
        adminRoleRepository.findByMember_Id(memberId).ifPresent(adminRoleRepository::delete);
        
        // 회원 삭제
        memberRepository.delete(member);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminDto.StudyResponse> getMemberLedStudies(Long adminId, Long memberId) {
        // 회원 조회 권한 체크
        checkPermission(adminId, AdminPermission.VIEW_ALL_MEMBERS);
        
        // 대상 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다: ID " + memberId));
        
        // 리더로 있는 스터디 조회
        List<Study> ledStudies = studyRepository.findByLeader_Id(memberId);
        
        return ledStudies.stream()
                .map(AdminDto.StudyResponse::from)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AdminDto.PasswordResetResponse resetMemberPassword(Long adminId, Long memberId, String tempPassword) {
        // 관리자 권한 체크 - 여기서는 별도 권한 지정 없이 관리자 여부만 확인
        Member admin = memberRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "관리자를 찾을 수 없습니다: ID " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다.");
        }
        
        // 대상 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다: ID " + memberId));
        
        // 임시 비밀번호 암호화 및 저장
        String encodedPassword = passwordEncoder.encode(tempPassword);
        member.changePassword(encodedPassword);
        memberRepository.save(member);
        
        // 응답 생성
        return AdminDto.PasswordResetResponse.builder()
                .memberId(member.getId())
                .userName(member.getUserName())
                .tempPassword(tempPassword)  // 암호화되지 않은 임시 비밀번호 반환
                .resetTime(LocalDateTime.now())
                .build();
    }
}
