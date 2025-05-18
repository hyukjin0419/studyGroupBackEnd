package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.StudyDto;
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
    public ResponseEntity<StudyDto.CreateResDto> createStudy(@RequestBody StudyDto.CreateReqDto request) {
        return ResponseEntity.ok(studyService.createStudy(request));
    }

    @Operation(summary = "스터디 단일 조회 API", description = "스터디를 단일 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDto.DetailResDto> getStudy(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyById(studyId));
    }

    @Operation(summary = "스터디 목록 조회 API", description = "스터디 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<StudyDto.ListResDto>> getAllStudies() {
        return ResponseEntity.ok(studyService.getAllStudies());
    }

    @Operation(summary = "스터디 업데이트 API", description = "스터디를 업데이트 합니다.")
    @PostMapping("/{studyId}")
    public ResponseEntity<StudyDto.UpdateResDto> updateStudy(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader_Id") Long leaderId,
            @RequestBody StudyDto.UpdateReqDto request) {
        return ResponseEntity.ok(studyService.updateStudy(studyId, leaderId, request));
    }

    @Operation(summary = "스터디 삭제 API")
    @DeleteMapping("/{studyId}")
    public ResponseEntity<StudyDto.DeleteResDto> deleteStudy(
            @PathVariable Long studyId,
            @RequestHeader("X-Leader_Id") Long leaderId){
        return ResponseEntity.ok(studyService.deleteStudy(studyId, leaderId));
    }
}
