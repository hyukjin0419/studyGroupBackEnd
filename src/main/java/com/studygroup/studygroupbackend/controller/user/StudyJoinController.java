package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.studymember.fellower.StudyJoinRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.StudyJoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
