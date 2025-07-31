package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.studyJoin.fellower.StudyInvitationAcceptResponse;
import com.studygroup.studygroupbackend.dto.studyJoin.fellower.StudyJoinRequest;
import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyMemberInvitationRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.StudyJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "StudyJoin", description = "스터디 초대/참여에 관한 API")
@RestController
@RequestMapping("/studies/")
@RequiredArgsConstructor
public class StudyJoinController {
    private final StudyJoinService studyJoinService;

    @Operation(summary = "스터디에 직접 참여 API", description = "멤버가 팀 코드를 사용해 팀에 참여합니다.")
    @PostMapping("/join")
    public ResponseEntity<Void> join (
            @RequestBody @Valid StudyJoinRequest request,
            @CurrentUser CustomUserDetails userDetails
    ) {
        studyJoinService.joinStudyByCode(userDetails.getMemberId(), request.getJoinCode());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디에 멤버 초청 API", description = "스터디에 멤버를 초청하고 해당하는 fcm을 발송합니다.")
    @PostMapping("/{studyId}/invite")
    public ResponseEntity<Void> inviteMember(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody List<StudyMemberInvitationRequest> requestList
    ) {
        Long leaderId = userDetails.getMemberId();
        studyJoinService.inviteMembers(leaderId, studyId, requestList);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스터디 초대 수락", description = "스터디 초대를 수락합니다")
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<StudyInvitationAcceptResponse> acceptInvitation(
            @PathVariable Long invitationId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        Long studyId = studyJoinService.acceptInvitation(invitationId, userDetails.getMemberId());
        return ResponseEntity.ok(StudyInvitationAcceptResponse.from(studyId));
    }

    @Operation(summary = "스터디 초대 거절", description = "스터디 초대를 거절합니다")
    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<Void> declineInvitation(
            @PathVariable Long invitationId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        studyJoinService.declineInvitation(invitationId, userDetails.getMemberId());
        return ResponseEntity.ok().build();
    }



//    @Operation(summary = "스터디에서 멤버 삭제 API", description = "스터디에서 멤버를 추방합니다.")
//    @DeleteMapping("/{memberId}")
//    public ResponseEntity<StudyMemberRemoveResponse> removeMember(
//            @PathVariable Long studyId,
//            @CurrentUser CustomUserDetails userDetails,
//            @PathVariable Long memberId){
//        Long leaderId = userDetails.getMemberId();
//        return ResponseEntity.ok(studyMemberService.removeMember(studyId, leaderId, memberId));
//    }

}
