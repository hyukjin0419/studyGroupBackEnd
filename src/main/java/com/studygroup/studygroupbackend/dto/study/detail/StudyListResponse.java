package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.dto.BaseResDto;
import com.studygroup.studygroupbackend.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyListResponse extends BaseResDto {
    private Long id;
    private String name;
    private String description;

    public static StudyListResponse fromEntity(Study study) {
        return StudyListResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
