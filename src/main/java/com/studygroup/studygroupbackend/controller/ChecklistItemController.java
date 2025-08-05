package com.studygroup.studygroupbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChecklistItem", description = "체크리스트 관련 API")
@RestController
@RequestMapping("/checklist-items")
@RequiredArgsConstructor
public class ChecklistItemController {
}
