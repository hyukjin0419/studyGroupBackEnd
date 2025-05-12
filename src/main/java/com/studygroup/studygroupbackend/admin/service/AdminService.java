package com.studygroup.studygroupbackend.admin.service;

import com.studygroup.studygroupbackend.admin.dto.AdminDto;

import java.util.List;

/**
 * Service interface for admin operations in the study group management system.
 * Contains methods for managing members, studies, and announcements with appropriate
 * permission checks.
 */
public interface AdminService {

    /**
     * Retrieves all members in the system.
     * Requires VIEW_ALL_MEMBERS permission.
     *
     * @param adminId The ID of the admin making the request
     * @return List of all members as DTOs
     */
    List<AdminDto.MemberResponse> getAllMembers(Long adminId);
    
    /**
     * Retrieves all study groups in the system.
     * Requires admin access.
     *
     * @param adminId The ID of the admin making the request
     * @return List of all study groups as DTOs
     */
    List<AdminDto.StudyResponse> getAllStudyGroups(Long adminId);

    /**
     * Force deletes a study group without the usual permission checks.
     * Requires FORCE_DELETE_STUDY permission.
     *
     * @param adminId The ID of the admin making the request
     * @param studyId The ID of the study to delete
     */
    void forceDeleteStudy(Long adminId, Long studyId);

    /**
     * Creates a new announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param adminId The ID of the admin making the request
     * @param request The announcement creation request
     * @return The created announcement as a DTO
     */
    AdminDto.AnnouncementResponse createAnnouncement(Long adminId, AdminDto.AnnouncementRequest request);

    /**
     * Updates an existing announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param adminId The ID of the admin making the request
     * @param announcementId The ID of the announcement to update
     * @param request The announcement update request
     * @return The updated announcement as a DTO
     */
    AdminDto.AnnouncementResponse updateAnnouncement(Long adminId, Long announcementId, AdminDto.AnnouncementRequest request);

    /**
     * Deletes an announcement.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param adminId The ID of the admin making the request
     * @param announcementId The ID of the announcement to delete
     */
    void deleteAnnouncement(Long adminId, Long announcementId);

    /**
     * Retrieves all announcements.
     * Requires MANAGE_ANNOUNCEMENTS permission.
     *
     * @param adminId The ID of the admin making the request
     * @return List of all announcements as DTOs
     */
    List<AdminDto.AnnouncementResponse> getAllAnnouncements(Long adminId);
    
    /**
     * Creates a new member.
     * Requires admin privileges.
     *
     * @param adminId The ID of the admin making the request
     * @param request The member creation request
     * @return The created member as a DTO
     */
    AdminDto.MemberResponse createMember(Long adminId, AdminDto.MemberRequest request);

    /**
     * Updates a member's information.
     * Requires admin privileges.
     *
     * @param adminId The ID of the admin making the request
     * @param memberId The ID of the member to update
     * @param request The member update request
     * @return The updated member as a DTO
     */
    AdminDto.MemberResponse updateMember(Long adminId, Long memberId, AdminDto.MemberRequest request);
    
    /**
     * 회원을 강제로 삭제하는 메소드
     * @param adminId 관리자 ID
     * @param memberId 삭제할 회원 ID
     */
    void forceDeleteMember(Long adminId, Long memberId);
    
    /**
     * 회원이 리더인 스터디 목록을 조회하는 메소드
     * @param adminId 관리자 ID
     * @param memberId 조회할 회원 ID
     * @return 해당 회원이 리더인 스터디 목록
     */
    List<AdminDto.StudyResponse> getMemberLedStudies(Long adminId, Long memberId);
    
    /**
     * 회원의 비밀번호를 강제로 초기화하는 메소드
     * @param adminId 관리자 ID
     * @param memberId 초기화할 회원 ID
     * @param tempPassword 임시 비밀번호
     * @return 초기화 결과
     */
    AdminDto.PasswordResetResponse resetMemberPassword(Long adminId, Long memberId, String tempPassword);
}
