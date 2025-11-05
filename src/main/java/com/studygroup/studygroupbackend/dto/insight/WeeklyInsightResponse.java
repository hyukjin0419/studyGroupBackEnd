package com.studygroup.studygroupbackend.dto.insight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyInsightResponse {
    private double completionRate;                  // 전체 완료율
    private int completedCount;                     // 완료 항목 수
    private int totalCount;                         // 전체 항목 수
    private int studyCount;                         // 참여한 스터디 수
    private List<DailyCompletionDto> dailyChecklistCompletion;  // 날짜별 완료 개수
    private List<StudyActivityDto> studyActivity;

    public static WeeklyInsightResponse buildEmptyResponse() {
        return WeeklyInsightResponse.builder()
                .completionRate(0)
                .completedCount(0)
                .totalCount(0)
                .studyCount(0)
                .dailyChecklistCompletion(Collections.emptyList())
                .studyActivity(Collections.emptyList())
                .build();
    }

    public static WeeklyInsightResponse buildResponse(
            double completionRate,
            int completedCount,
            int totalCount,
            int studyCount,
            List<DailyCompletionDto> dailyChecklistCompletion,
            List<StudyActivityDto> studyActivity
    ) {
        return WeeklyInsightResponse.builder()
                .completionRate(completionRate)
                .completedCount(completedCount)
                .totalCount(totalCount)
                .studyCount(studyCount)
                .dailyChecklistCompletion(dailyChecklistCompletion)
                .studyActivity(studyActivity)
                .build();
    }
}
