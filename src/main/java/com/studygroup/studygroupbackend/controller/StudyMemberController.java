package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.studymember.StudyMemberInviteRequest;
import com.studygroup.studygroupbackend.dto.studymember.StudyMemberInviteResponse;
import com.studygroup.studygroupbackend.dto.studymember.StudyMemberRemoveResponse;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.StudyMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "StudyMember", description = "스터디 멤버에 관한 API")
@RestController
@RequestMapping("/studies/{studyId}/members")
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    /*
    멤버 초대하는 시나리오를 어떻게 할까?

    1. 리더가 아이디를 통해서 멤버 초대
    2. 푸시 알림 가고
    3. 멤버가 수락 하면
    4. 초대 완료

    ------------------------------
    어떻게 구현?
    - 일단 New Table 2개 필요

    1. StudyInvite 테이블
    - id
    - study (FK)
    - inviter (FK, Member)
    - invitee (FK, Member)
    - InviteStatus status (PENDING, ACCEPTED, REJECTED)

--->>>> 여기서 부터 알림에 대해 다시 고민해보쟈
    2. Notification
    - id
    - recipient (FK, Member) 알림을 받을 사용자
    - Notification type
    - referencedId...?
    - message

    public enum NotificationType {
    INVITE,
    CHECKLIST_ASSIGNED,
    CHECKLIST_COMPLETED,
    STUDY_UPDATED,
    SYSTEM_MESSAGE
}
     */
    @Operation(summary = "스터디에 멤버 초청 API", description = "스터디에 멤버를 초청합니다.")
    @PostMapping
    public ResponseEntity<StudyMemberInviteResponse> inviteMember(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody StudyMemberInviteRequest request) {
        Long leaderId = userDetails.getMemberId();
        return ResponseEntity.ok(studyMemberService.inviteMember(studyId, leaderId, request));
    }

    @Operation(summary = "스터디에서 멤버 삭제 API", description = "스터디에서 멤버를 추방합니다.")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<StudyMemberRemoveResponse> removeMember(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long memberId){
        Long leaderId = userDetails.getMemberId();
        return ResponseEntity.ok(studyMemberService.removeMember(studyId, leaderId, memberId));
    }
}
