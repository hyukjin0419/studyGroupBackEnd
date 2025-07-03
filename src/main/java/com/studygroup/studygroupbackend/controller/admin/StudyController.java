package com.studygroup.studygroupbackend.controller.admin;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class StudyController {

    //우선 담아놓기
//    @Operation(summary = "스터디 생성 API", description = "[USER] 새로운 스터디를 등록합니다.")
//    @PostMapping
//    public ResponseEntity<StudyCreateResponse> createStudy(
//            @CurrentUser CustomUserDetails userDetails,
//            @RequestBody StudyCreateRequest request) {
//        return ResponseEntity.ok(studyService.createStudy(userDetails.getMemberId(),request));
//    }
    /*
    @Operation(summary = "스터디 단일 조회 API", description = "스터디를 단일 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> getStudy(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyById(studyId));
    }

    @Operation(summary = "스터디 목록 조회 API", description = "스터디 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<StudyListResponse>> getAllStudies() {
        return ResponseEntity.ok(studyService.getAllStudies());
    }

    @Operation(summary = "스터디 업데이트 API", description = "스터디를 업데이트 합니다.")
    @PostMapping("/{studyId}")
    public ResponseEntity<StudyDetailResponse> updateStudy(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody StudyUpdateRequest request) {
        return ResponseEntity.ok(studyService.updateStudy(studyId, userDetails.getMemberId(), request));
    }

    @Operation(summary = "스터디 삭제 API")
    @DeleteMapping("/{studyId}")
    public ResponseEntity<StudyDeleteResponse> deleteStudy(
            @PathVariable Long studyId,
            @CurrentUser CustomUserDetails userDetails
            ){
        return ResponseEntity.ok(studyService.deleteStudy(studyId, userDetails.getMemberId()));
    }
     */
}
