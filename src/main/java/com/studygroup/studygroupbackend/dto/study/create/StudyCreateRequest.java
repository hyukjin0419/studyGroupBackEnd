package com.studygroup.studygroupbackend.dto.study.create;

import com.studygroup.studygroupbackend.domain.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyCreateRequest{
    private String name;
    private String description;
    private String color;
    private LocalDateTime dueDate;

    public Study toEntity() {
        return Study.of(
                name,
                description,
                color,
                dueDate,
                null
        );
    }
}

