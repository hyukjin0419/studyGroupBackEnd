package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.domain.ChecklistItem;
import com.studygroup.studygroupbackend.dto.insight.DailyCompletionDto;
import com.studygroup.studygroupbackend.dto.insight.StudyActivityDto;
import com.studygroup.studygroupbackend.dto.insight.WeeklyInsightResponse;
import com.studygroup.studygroupbackend.repository.ChecklistItemRepository;
import com.studygroup.studygroupbackend.service.InsightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class InsightServiceImpl implements InsightService {

    private final ChecklistItemRepository checklistItemRepository;

    @Override
    public WeeklyInsightResponse getWeeklyInsight(Long memberId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        List<ChecklistItem> items = checklistItemRepository.findAllByStudyMember_Member_IdAndTargetDateBetween(memberId,startDate,endDate);

        if(items.isEmpty()) return WeeklyInsightResponse.buildResponse(0.0,0,0,0, Collections.emptyList(),Collections.emptyList());

        long completedCount = items.stream().filter(ChecklistItem::isCompleted).count();
        int totalCount = items.size();
        double completionRate = (double) completedCount / totalCount;

        //날짜별 완료 개수
        Map<LocalDate, Long> dailyMap = items.stream()
                .filter(ChecklistItem::isCompleted)
                .collect(Collectors.groupingBy(ChecklistItem::getTargetDate, Collectors.counting()));

        List<DailyCompletionDto> dailyList = dailyMap.entrySet().stream()
                .map(e -> new DailyCompletionDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(DailyCompletionDto::getDate))
                .toList();

        //스터디별 활동도 (내가 속한 스터디별 완류율)
        Map<Long, List<ChecklistItem>> byStudy = items.stream()
                .collect(Collectors.groupingBy(i -> i.getStudy().getId()));

        List<StudyActivityDto> studyActivityList = byStudy.entrySet().stream()
                .map(entry -> {
                    List<ChecklistItem> studyItems = entry.getValue();
                    long studyCompleted = studyItems.stream().filter(ChecklistItem::isCompleted).count();
                    double rate = (double) studyCompleted / studyItems.size();
                    return new StudyActivityDto(
                            entry.getKey(),
                            studyItems.get(0).getStudy().getName(),
                            rate
                    );
                })
                .sorted(Comparator.comparing(StudyActivityDto::getActivityRate).reversed())
                .toList();

        return WeeklyInsightResponse.buildResponse(
                completionRate,
                (int) completedCount,
                totalCount,
                studyActivityList.size(),
                dailyList,
                studyActivityList
        );
    }
}
