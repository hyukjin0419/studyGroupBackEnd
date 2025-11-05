package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.insight.WeeklyInsightResponse;

import java.time.LocalDate;

public interface InsightService {
    WeeklyInsightResponse getWeeklyInsight(Long memberId, LocalDate startDate);
}
