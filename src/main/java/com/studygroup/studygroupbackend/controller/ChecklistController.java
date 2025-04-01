package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.ChecklistDto;
import com.studygroup.studygroupbackend.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklist")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @Operation(summary = "체크리스트 생성 API")
    @PostMapping
    public ResponseEntity<ChecklistDto.CreateResDto> createChecklist(
            @RequestHeader("X-Member-Id") Long creatorId,
            @RequestBody ChecklistDto.CreateReqDto request) {
        return ResponseEntity.ok(checklistService.createChecklist(creatorId, request));
    }

    @Operation(summary = "체크리스트 컨텐츠 업데이트 API")
    @PatchMapping("/{id}/content")
    public ResponseEntity<Void> updateContent(
            @PathVariable Long id,
            @RequestBody ChecklistDto.UpdateContentReqDto request) {
        checklistService.updateContent(id, request.getContent());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "체크리스트 날짜 업데이트 API")
    @PatchMapping("/{id}/duedate")
    public ResponseEntity<Void> updateDueDate(
            @PathVariable Long id,
            @RequestBody ChecklistDto.UpdateDueDateReqDto request) {
        checklistService.updateDueDate(id, request.getDueDate());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "체크리스트 단일 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDto.DetailResDto> getChecklistDetail(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getChecklistDetail(id));
    }

    @Operation(summary = "체크리스트 삭제 API")
    @DeleteMapping("{id}")
    public ResponseEntity<ChecklistDto.DeleteResDto> deleteChecklist(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.deleteChecklist(id));
    }
}
