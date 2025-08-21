package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemCreateRequest;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemContentUpdateRequest;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.ChecklistItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "ChecklistItem", description = "체크리스트 관련 API")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ChecklistItemController {
    private final ChecklistItemService checklistItemService;

    @Operation(summary = "단일 체크리스트 아이템 작성 및 단일 할당 API", description = "[CHECK_LIST_ITEM] 새로운 단일 체크리스트를 생성 후 팀내 단일 멤버에게 적용합니다.")
    @PostMapping("/studies/{studyId}/checklistItem/create")
    public ResponseEntity<Void> createChecklistItemOfStudy(
            @CurrentUser CustomUserDetails userDetails,
            @PathVariable Long studyId,
            @RequestBody ChecklistItemCreateRequest request
    ) {
        checklistItemService.createChecklistItemOfStudy(userDetails.getMemberId(),studyId,request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "단일 스터디 내부 체크리스트 아이템 조회", description = "[CHECK_LIST_ITEM] 단일 스터디에 속한 체크리스트 조회.")
    @GetMapping("studies/{studyId}/checklists")
    public ResponseEntity<List<ChecklistItemDetailResponse>> getStudyChecklistItemsByDate(
            @PathVariable Long studyId,
            @RequestParam("targetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ){
        List<ChecklistItemDetailResponse> items = checklistItemService.getStudyItemsByDate(studyId, targetDate);
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
//
//    @Operation(summary = "단일 체크리스트 아이템 삭제", description = "[CHECK_LIST_ITEM] 단일 체크리스트 아이템 삭제")

}
