package com.studygroup.studygroupbackend.dto.study.delete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyDeleteResponse{
    private String message;

    public static StudyDeleteResponse successDelete(){
        return StudyDeleteResponse.builder()
                .message("스터디 삭제 완료")
                .build();
    }
}
