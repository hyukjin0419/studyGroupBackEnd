package com.studygroup.studygroupbackend.controller.user;

import com.studygroup.studygroupbackend.dto.insight.WeeklyInsightResponse;
import com.studygroup.studygroupbackend.security.annotation.CurrentUser;
import com.studygroup.studygroupbackend.security.domain.CustomUserDetails;
import com.studygroup.studygroupbackend.service.InsightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Insight", description = "사용자 본인 인사이트 탭용 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/me/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;

    @GetMapping("/weekly")
    public WeeklyInsightResponse getWeeklyInsight(@CurrentUser CustomUserDetails userDetails,
                                                  @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        return insightService.getWeeklyInsight(userDetails.getMemberId(), startDate);
    }
}
