package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.checklistItem.ChecklistItemDetailResponse;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.PersonalChecklistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "PersonalChecklistController", description = "사용자 개인 화면 체크리스트 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/personal")
@RequiredArgsConstructor
public class PersonalChecklistController {
    private final PersonalChecklistService personalChecklistService;

    @GetMapping
    public List<ChecklistItemDetailResponse> getStudyChecklistItemsByWeek(
            @CurrentUser CustomUserDetails userDetails,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {

        return personalChecklistService.getPersonalChecklists(userDetails.getMemberId(), startDate);
    }

}
