package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.StudyDto;
import com.studygroup.studygroupbackend.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 생성 API")
    @PostMapping
    public ResponseEntity<StudyDto.CreateResDto> createStudy(@RequestBody StudyDto.CreateReqDto request) {
        return ResponseEntity.ok(studyService.createStudy(request));
    }

    @Operation(summary = "스터디 단일 조회 API")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDto.DetailResDto> getStudy(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudyById(studyId));
    }

    @Operation(summary = "스터디 목록 조회 API")
    @GetMapping
    public ResponseEntity<List<StudyDto.ListResDto>> getAllStudies() {
        return ResponseEntity.ok(studyService.getAllStudies());
    }

    @Operation(summary = "스터디 업데이트 API")
    @PutMapping("/{studyId}")
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
