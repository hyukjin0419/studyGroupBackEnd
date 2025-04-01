package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.ChecklistDto;
import com.studygroup.studygroupbackend.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklist")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @PostMapping
    public ResponseEntity<ChecklistDto.CreateResDto> createChecklist(
            @RequestHeader("X-Member-Id") Long creatorId,
            @RequestBody ChecklistDto.CreateReqDto request) {
        return ResponseEntity.ok(checklistService.createChecklist(creatorId, request));
    }

    @PatchMapping("/{id}/content")
    public ResponseEntity<Void> updateContent(
            @PathVariable Long id,
            @RequestBody ChecklistDto.UpdateContentReqDto request) {
        checklistService.updateContent(id, request.getContent());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/duedate")
    public ResponseEntity<Void> updateDueDate(
            @PathVariable Long id,
            @RequestBody ChecklistDto.UpdateDueDateReqDto request) {
        checklistService.updateDueDate(id, request.getDueDate());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDto.DetailResDto> getChecklistDetail(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getChecklistDetail(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ChecklistDto.DeleteResDto> deleteChecklist(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.deleteChecklist(id));
    }
}
