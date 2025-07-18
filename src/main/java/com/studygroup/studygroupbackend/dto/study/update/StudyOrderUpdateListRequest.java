package com.studygroup.studygroupbackend.dto.study.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyOrderUpdateListRequest {
    private List<StudyOrderUpdateRequest> orderList;
}
