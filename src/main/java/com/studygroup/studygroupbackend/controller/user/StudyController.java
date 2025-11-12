package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyOrderUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Study", description = "스터디 생성, 수정, 삭제등 스터디 자체에 대한 API")
@Slf4j
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "본인이 리더로써 스터디 생성 API", description = "[USER] 새로운 스터디를 리더로써 등록합니다.")
    @PostMapping("/create")
    //TODO 반환 값 Void로 변환 바람
    public ResponseEntity<StudyCreateResponse> createStudy(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody StudyCreateRequest request) {
        return ResponseEntity.ok(studyService.createStudy(userDetails.getMemberId(),request));
    }

    //필요한 함수
    //1. 내가 속한 studies 조회
    @Operation(summary = "본인이 속한 스터디 전체 조회 API", description = "[USER] 본인이 속한 스터디 전체를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<StudyDetailResponse>> getMyStudies(@CurrentUser CustomUserDetails userDetails) {
        return ResponseEntity.ok(studyService.getStudiesByMemberIdAsc(userDetails.getMemberId()));
    }

    //2. 스터디 단일 조회 (조회시 자신이 속한 스터디인지 검증 해야함) -> 이건 [USER]
    @Operation(summary = "본인이 속한 스터디 단일 조회 API", description = "[USER] 본인이 속한 스터디 중 단일 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> getMyStudy(@CurrentUser CustomUserDetails userDetails, @PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyByMemberIdAndStudyId(userDetails.getMemberId(),studyId));
    }

    //3. [USER] update
    @Operation(summary = "본인이 리더로 속한 단일 스터디 업데이트 API", description = "[USER] 리더가 스터디를 업데이트 합니다.")
    @PostMapping("/update")
    public ResponseEntity<StudyDetailResponse> updateStudy(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody StudyUpdateRequest request
    ) {
        return ResponseEntity.ok(studyService.updateStudy(userDetails.getMemberId(), request));
    }


    //3.1 [USER] 순서 업데이트
    @Operation(summary = "본인 화면에 팀 카드 순서 업데이트 API", description = "[USER] 본인 팀의 순서를 Drag & Drop을 통해 업데이트 합니다.")
    @PostMapping("/update-order")
    public ResponseEntity<Void> updateStudiesOrder(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody List<StudyOrderUpdateRequest> request
    ) {
        studyService.updateStudyOrder(userDetails.getMemberId(),request);
        return ResponseEntity.ok().build();
    }

    //4. [USER] delete
    @Operation(summary = "본인이 리더로 속한 단일 스터디 삭제 API", description = "[USER] 리더가 스터디를 삭제 합니다.")
    @DeleteMapping("/delete/{studyId}")
    public ResponseEntity<StudyDeleteResponse> deleteStudy(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails
    ){
        return ResponseEntity.ok(studyService.deleteStudy(studyId, userDetails.getMemberId()));
    }
}
