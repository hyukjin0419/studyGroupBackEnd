package com.studygroup.studygroupbackend.admin.controller;

import com.studygroup.studygroupbackend.admin.dto.AdminDto;
import com.studygroup.studygroupbackend.admin.service.AdminService;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Controller for administrative operations.
 * Provides endpoints for managing members, studies, and announcements.
 * All endpoints require authentication and appropriate permissions.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberRepository memberRepository;

    /**
     * Endpoint to retrieve all members in the system.
     * Requires VIEW_ALL_MEMBERS permission.
     *
     * @return List of all members
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllMembers(@RequestParam(required = false) Long adminId) {
        // adminId 파라미터가 없으면 인증에서 가져오기
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        List<AdminDto.MemberResponse> members = adminService.getAllMembers(actualAdminId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", members
        ));
    }
    
    /**
     * Endpoint to retrieve all study groups in the system.
     * Requires admin access.
     *
     * @return List of all study groups
     */
    @GetMapping("/groups")
    public ResponseEntity<Map<String, Object>> getAllStudyGroups(@RequestParam(required = false) Long adminId) {
        // adminId 파라미터가 없으면 인증에서 가져오기
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        List<AdminDto.StudyResponse> groups = adminService.getAllStudyGroups(actualAdminId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", groups
        ));
    }

    /**
     * Endpoint to force delete a study group.
     * Requires FORCE_DELETE_STUDY permission.
     *
     * @param groupId The ID of the study group to delete
     * @return Success message
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Map<String, Object>> forceDeleteStudy(
            @PathVariable Long groupId,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        adminService.forceDeleteStudy(actualAdminId, groupId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "스터디 그룹이 성공적으로 삭제되었습니다."
        ));
    }

    /**
     * Endpoint to retrieve all announcements.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @return List of all announcements
     */
    @GetMapping("/announcements")
    public ResponseEntity<Map<String, Object>> getAllAnnouncements(@RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        List<AdminDto.AnnouncementResponse> announcements = adminService.getAllAnnouncements(actualAdminId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", announcements
        ));
    }
    
    /**
     * Endpoint to create a new announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param request The announcement creation request
     * @return The created announcement
     */
    @PostMapping("/announcements")
    public ResponseEntity<Map<String, Object>> createAnnouncement(
            @RequestBody AdminDto.AnnouncementRequest request,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        AdminDto.AnnouncementResponse response = adminService.createAnnouncement(actualAdminId, request);
        return new ResponseEntity<>(Map.of(
                "success", true,
                "message", "공지사항이 성공적으로 등록되었습니다.",
                "data", response
        ), HttpStatus.CREATED);
    }

    /**
     * Endpoint to update an existing announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param announcementId The ID of the announcement to update
     * @param request The announcement update request
     * @return The updated announcement
     */
    @PutMapping("/announcements/{announcementId}")
    public ResponseEntity<Map<String, Object>> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody AdminDto.AnnouncementRequest request,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        AdminDto.AnnouncementResponse response = adminService.updateAnnouncement(actualAdminId, announcementId, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "공지사항이 성공적으로 수정되었습니다.",
                "data", response
        ));
    }

    /**
     * Endpoint to delete an announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param announcementId The ID of the announcement to delete
     * @return Success message
     */
    @DeleteMapping("/announcements/{announcementId}")
    public ResponseEntity<Map<String, Object>> deleteAnnouncement(
            @PathVariable Long announcementId,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        adminService.deleteAnnouncement(actualAdminId, announcementId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "공지사항이 성공적으로 삭제되었습니다."
        ));
    }
    
    /**
     * Endpoint to create a new member.
     * Requires admin privileges.
     *
     * @param request The member creation request
     * @return The created member
     */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createMember(
            @RequestBody AdminDto.MemberRequest request,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        AdminDto.MemberResponse response = adminService.createMember(actualAdminId, request);
        return new ResponseEntity<>(Map.of(
                "success", true,
                "message", "새 사용자가 성공적으로 등록되었습니다.",
                "data", response
        ), HttpStatus.CREATED);
    }
    
    /**
     * Endpoint to force delete a member.
     * Requires FORCE_DELETE_MEMBER permission.
     *
     * @param memberId The ID of the member to delete
     * @return Success message
     */
    @DeleteMapping("/users/{memberId}")
    public ResponseEntity<Map<String, Object>> forceDeleteMember(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        adminService.forceDeleteMember(actualAdminId, memberId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원이 성공적으로 삭제되었습니다."
        ));
    }
    
    /**
     * 회원이 리더인 스터디 목록을 조회하는 엔드포인트
     * 
     * @param memberId 조회할 회원 ID
     * @param adminId 관리자 ID (선택적)
     * @return 해당 회원이 리더인 스터디 목록
     */
    @GetMapping("/users/{memberId}/led-studies")
    public ResponseEntity<Map<String, Object>> getMemberLedStudies(
            @PathVariable Long memberId,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        List<AdminDto.StudyResponse> studies = adminService.getMemberLedStudies(actualAdminId, memberId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", studies
        ));
    }
    
    /**
     * 회원의 비밀번호를 초기화하는 엔드포인트
     * 
     * @param memberId 초기화할 회원 ID
     * @param request 비밀번호 초기화 요청 데이터
     * @param adminId 관리자 ID (선택적)
     * @return 초기화된 비밀번호 정보
     */
    @PostMapping("/users/{memberId}/reset-password")
    public ResponseEntity<Map<String, Object>> resetMemberPassword(
            @PathVariable Long memberId,
            @RequestBody Map<String, String> request,
            @RequestParam(required = false) Long adminId) {
        
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        String tempPassword = request.get("tempPassword");
        
        if (tempPassword == null || tempPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "임시 비밀번호가 제공되지 않았습니다.");
        }
        
        AdminDto.PasswordResetResponse response = adminService.resetMemberPassword(actualAdminId, memberId, tempPassword);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "비밀번호가 성공적으로 초기화되었습니다.",
                "data", response
        ));
    }
    
    /**
     * Endpoint to update a member's information.
     * Requires admin privileges.
     *
     * @param memberId The ID of the member to update
     * @param request The member update request
     * @return The updated member
     */
    @PutMapping("/users/{memberId}")
    public ResponseEntity<Map<String, Object>> updateMember(
            @PathVariable Long memberId,
            @RequestBody AdminDto.MemberRequest request,
            @RequestParam(required = false) Long adminId) {
        Long actualAdminId = (adminId != null) ? adminId : getAdminIdFromAuth();
        AdminDto.MemberResponse response = adminService.updateMember(actualAdminId, memberId, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원 정보가 성공적으로 수정되었습니다.",
                "data", response
        ));
    }
    
    /**
     * 인증된 관리자의 ID를 반환하는 유틸리티 메서드
     * 인증 정보에서 사용자 ID를 추출
     * 
     * @return 관리자 ID
     */
    /**
     * 현재 로그인한 관리자 정보를 반환하는 엔드포인트
     * 클라이언트에서 관리자 정보를 표시하고 API 호출에 사용하기 위해 사용됨
     * 
     * @return 관리자 정보 (ID, 사용자명 등)
     */
    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증된 사용자가 없습니다.");
        }
        
        String username = authentication.getName();
        Member member = memberRepository.findByUserName(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다."));
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "id", member.getId(),
            "userName", member.getUserName(),
            "email", member.getEmail(),
            "role", member.getRole().name()
        ));
    }
    
    /**
     * CSRF 토큰을 제공하는 엔드포인트
     * 클라이언트에서 POST, PUT, DELETE 요청을 보낼 때 필요한 CSRF 토큰을 제공
     * 특히 로그아웃 처리와 같이 폼 전송시 필요
     * 
     * @param request HTTP 요청 객체
     * @return CSRF 토큰 정보
     */
    @GetMapping("/get-csrf")
    public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        if (csrf != null) {
            return ResponseEntity.ok(Map.of(
                "parameterName", csrf.getParameterName(),
                "token", csrf.getToken(),
                "headerName", csrf.getHeaderName()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "parameterName", "_csrf",
                "token", "",
                "headerName", "X-CSRF-TOKEN"
            ));
        }
    }
    
    private Long getAdminIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        
        // 인증 정보에서 사용자명(username)을 가져와 해당 사용자 ID를 조회
        String username = authentication.getName();
        return memberRepository.findByUserName(username)
            .map(Member::getId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자입니다."));
    }
}
