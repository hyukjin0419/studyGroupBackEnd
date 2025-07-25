package com.studygroup.studygroupbackend.dto;

import com.studygroup.studygroupbackend.domain.StudyTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyTimeDto {

    private Long id;
    private Long memberId;
    private LocalDate date;
    private int totalMinutes;

    public static StudyTimeDto fromEntity(StudyTime studyTime) {
        return new StudyTimeDto(
                studyTime.getId(),
                studyTime.getMember().getId(),
                studyTime.getDate(),
                studyTime.getTotalMinutes()
        );
    }
}
