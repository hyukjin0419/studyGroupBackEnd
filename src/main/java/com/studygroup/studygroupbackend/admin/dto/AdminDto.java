package com.studygroup.studygroupbackend.admin.dto;

import com.studygroup.studygroupbackend.entity.Announcement;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class AdminDto {
    
    // DTO for password reset response
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordResetResponse {
        private Long memberId;
        private String userName;
        private String tempPassword;
        private LocalDateTime resetTime;
    }

    /**
     * Request and response DTOs for admin operations
     */
    
    // DTO for member update request
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberRequest {
        private String userName;
        private String email;
    }
    
    // DTO for member listing
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberResponse {
        private Long id;
        private String userName;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public static MemberResponse from(Member member) {
            return MemberResponse.builder()
                    .id(member.getId())
                    .userName(member.getUserName())
                    .email(member.getEmail())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .build();
        }
    }
    
    // DTO for admin operations on studies
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyResponse {
        private Long id;
        private String name;
        private String description;
        private MemberResponse leader;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public static StudyResponse from(Study study) {
            return StudyResponse.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .description(study.getDescription())
                    .leader(MemberResponse.from(study.getLeader()))
                    .createdAt(study.getCreatedAt())
                    .updatedAt(study.getUpdatedAt())
                    .build();
        }
    }
    
    // DTOs for announcement operations
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnouncementRequest {
        private String title;
        private String content;
        private LocalDateTime publishDate; // 게시일: 경과된 날짜는 바로 게시, 미래 날짜는 임시저장 상태
        private LocalDateTime expiryDate; // Optional, can be null
        private boolean important;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnouncementResponse {
        private Long id;
        private String title;
        private String content;
        private MemberResponse author;
        private LocalDateTime publishDate;
        private LocalDateTime expiryDate;
        private boolean important;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public static AnnouncementResponse from(Announcement announcement) {
            return AnnouncementResponse.builder()
                    .id(announcement.getId())
                    .title(announcement.getTitle())
                    .content(announcement.getContent())
                    .author(MemberResponse.from(announcement.getAuthor()))
                    .publishDate(announcement.getPublishDate())
                    .expiryDate(announcement.getExpiryDate())
                    .important(announcement.isImportant())
                    .createdAt(announcement.getCreatedAt())
                    .updatedAt(announcement.getUpdatedAt())
                    .build();
        }
    }
    
    // Common response wrapper
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;
        
        public static <T> ApiResponse<T> of(String message, T data) {
            return new ApiResponse<>(message, data);
        }
    }
}
