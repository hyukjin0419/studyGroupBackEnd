package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemContentUpdateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemReorderRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "ChecklistItem", description = "체크리스트 관련 API")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class ChecklistItemController {
    private final ChecklistItemService checklistItemService;

    @Operation(summary = "단일 체크리스트 아이템 작성 및 단일 할당 API", description = "[CHECK_LIST_ITEM] 새로운 단일 체크리스트를 생성 후 팀내 단일 멤버에게 적용합니다.")
    @PostMapping("/studies/{studyId}/checklistItem/create")
    public ResponseEntity<ChecklistItemDetailResponse> createChecklistItemOfStudy(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long studyId,
            @RequestBody ChecklistItemCreateRequest request
    ) {
        return ResponseEntity.ok(checklistItemService.createChecklistItemOfStudy(userDetails.getMemberId(),studyId,request));
    }

    @Operation(summary = "단일 스터디 내부 체크리스트 아이템 일 단위 조회", description = "[CHECK_LIST_ITEM] 단일 스터디에 속한 체크리스트를 일 단위로 조회합니다.")
    @GetMapping("studies/{studyId}/checklists")
    public ResponseEntity<List<ChecklistItemDetailResponse>> getStudyChecklistItemsByDate(
            @PathVariable Long studyId,
            @RequestParam("targetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ){
        List<ChecklistItemDetailResponse> items = checklistItemService.getStudyItemsByDate(studyId, targetDate);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "단일 스터디 내부 Date별 체크리스트 아이템 주 단위 조회", description = "[CHECK_LIST_ITEM] 단일 스터디에 속한 체크리스트를 주 단위로 조회합니다.")
    @GetMapping("studies/{studyId}/checklists/week")
    public ResponseEntity<List<ChecklistItemDetailResponse>> getStudyChecklistItemsByWeek(
            @PathVariable Long studyId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ){
        List<ChecklistItemDetailResponse> items = checklistItemService.getStudyItemsByWeek(studyId, startDate);
        return ResponseEntity.ok(items);
    }

    @Operation(summary = "단일 체크리스트 아이템 수정", description = "[CHECK_LIST_ITEM] 단일 체크리스트 아이템 수정")
    @PostMapping("/checklistItem/{checklistItemId}")
    public ResponseEntity<Void> updateChecklistItemContent(
            @PathVariable Long checklistItemId,
            @RequestBody ChecklistItemContentUpdateRequest request
    ) {
        checklistItemService.updateChecklistItemContent(checklistItemId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "단일 체크리스트 아이템 체크값 변경", description = "[CHECK_LIST_ITEM] 단일 체크리스트 아이템 체크값 변경")
    @PostMapping("/checklistItem/{checklistItemId}/changeCheckStatus")
    public ResponseEntity<Void> updateCheckItemStatus(
            @PathVariable Long checklistItemId
    ) {
        checklistItemService.updateChecklistItemStatus(checklistItemId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "체크리스트 Drag & Drop으로 인한 화면상 순서 변경", description = "체크리스트 Drag & Drop으로 인한 화면상 순서 변경")
    @PostMapping("/checklistItem/reorder")
    public ResponseEntity<Void> reorderChecklistItems(
            @RequestBody List<ChecklistItemReorderRequest> requestList
    ) {
        checklistItemService.reorderChecklistItems(requestList);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "단일 체크리스트 아이템 삭제", description = "[CHECK_LIST_ITEM] 단일 체크리스트 아이템 삭제")
    @PostMapping("/checklistItem/{checklistItemId}/delete")
    public ResponseEntity<Void> softDeleteChecklistItems(
            @PathVariable Long checklistItemId
    ) {
        checklistItemService.softDeleteChecklistItems(checklistItemId);
        return ResponseEntity.ok().build();
    }
}
