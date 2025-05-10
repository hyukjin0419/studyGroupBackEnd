package com.studygroup.studygroupbackend.admin.controller;

import com.studygroup.studygroupbackend.admin.dto.AdminDto;
import com.studygroup.studygroupbackend.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Endpoint to retrieve all members in the system.
     * Requires VIEW_ALL_MEMBERS permission.
     *
     * @return List of all members
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllMembers() {
        List<AdminDto.MemberResponse> members = adminService.getAllMembers(getAdminIdFromAuth());
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
    public ResponseEntity<Map<String, Object>> getAllStudyGroups() {
        List<AdminDto.StudyResponse> groups = adminService.getAllStudyGroups(getAdminIdFromAuth());
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
    public ResponseEntity<Map<String, Object>> forceDeleteStudy(@PathVariable Long groupId) {
        adminService.forceDeleteStudy(getAdminIdFromAuth(), groupId);
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
    public ResponseEntity<Map<String, Object>> getAllAnnouncements() {
        List<AdminDto.AnnouncementResponse> announcements = adminService.getAllAnnouncements(getAdminIdFromAuth());
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
    public ResponseEntity<Map<String, Object>> createAnnouncement(@RequestBody AdminDto.AnnouncementRequest request) {
        AdminDto.AnnouncementResponse response = adminService.createAnnouncement(getAdminIdFromAuth(), request);
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
            @RequestBody AdminDto.AnnouncementRequest request) {
        AdminDto.AnnouncementResponse response = adminService.updateAnnouncement(getAdminIdFromAuth(), announcementId, request);
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
    public ResponseEntity<Map<String, Object>> deleteAnnouncement(@PathVariable Long announcementId) {
        adminService.deleteAnnouncement(getAdminIdFromAuth(), announcementId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "공지사항이 성공적으로 삭제되었습니다."
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
            @RequestBody AdminDto.MemberRequest request) {
        AdminDto.MemberResponse response = adminService.updateMember(getAdminIdFromAuth(), memberId, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원 정보가 성공적으로 수정되었습니다.",
                "data", response
        ));
    }
    
    /**
     * 인증된 관리자의 ID를 반환하는 유틸리티 메서드
     * 현재는 고정된 값을 반환하지만, 실제 구현에서는 인증 정보에서 사용자 ID를 추출해야 함
     * 
     * @return 관리자 ID
     */
    private Long getAdminIdFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        
        // 실제 구현에서는 인증 정보에서 사용자 ID를 추출
        // 임시로 ID 1을 가진 관리자로 고정
        return 1L;
    }
}
