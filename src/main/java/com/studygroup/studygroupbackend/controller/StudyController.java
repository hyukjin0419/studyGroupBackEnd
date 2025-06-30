package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Study", description = "스터디 관련 API")
@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 생성 API", description = "새로운 스터디를 등록합니다.")
    @PostMapping
    public ResponseEntity<StudyCreateResponse> createStudy(
            @CurrentUser CustomUserDetails userDetails,
            @RequestBody StudyCreateRequest request) {
        return ResponseEntity.ok(studyService.createStudy(userDetails.getMemberId(),request));
    }

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
}
