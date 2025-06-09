package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistitem.create.ChecklistItemCreateResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.delete.ChecklistItemDeleteResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.detail.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistitem.update.ChecklistItemUpdateRequest;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ChecklistItem", description = "체크리스트 관련 API")
@RestController
@RequestMapping("/checklist-items")
@RequiredArgsConstructor
public class ChecklistItemController {

    private final ChecklistItemService checklistItemService;

    @Operation(summary = "체크리스트 생성 API", description = "새로운 체크리스트를 등록합니다.")
    @PostMapping
    public ResponseEntity<ChecklistItemCreateResponse> createChecklistItem(
            @RequestHeader("X-Member-Id") Long creatorId,
            @RequestBody ChecklistItemCreateRequest request) {
        return ResponseEntity.ok(checklistItemService.createChecklistItem(creatorId, request));
    }

    @Operation(summary = "체크리스트 업데이트 API", description = "체크리스트 날짜를 업데이트 합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateChecklistItem(
            @PathVariable Long id,
            @RequestBody ChecklistItemUpdateRequest request) {
        checklistItemService.updateChecklistItem(id, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "체크리스트 단일 조회 API", description = "단일 체크리스트를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ChecklistItemDetailResponse> getChecklistItemDetail(@PathVariable Long id) {
        return ResponseEntity.ok(checklistItemService.getChecklistItemDetail(id));
    }

    @Operation(summary = "체크리스트 삭제 API", description = "체크리스트를 삭제합니다.")
    @DeleteMapping("{id}")
    public ResponseEntity<ChecklistItemDeleteResponse> deleteChecklistItem(@PathVariable Long id) {
        return ResponseEntity.ok(checklistItemService.deleteChecklistItem(id));
    }
}
