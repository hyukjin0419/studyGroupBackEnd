package com.studygroup.studygroupbackend.controller;

import com.studygroup.studygroupbackend.service.ChecklistItemAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChecklistItemAssignment", description = "체크리스트 할당 관련 API")
@RestController
@RequestMapping("/checklist-item-assignments")
@RequiredArgsConstructor
public class ChecklistItemAssignmentController {

    private final ChecklistItemAssignmentService checklistItemAssignmentService;

//    @
}
