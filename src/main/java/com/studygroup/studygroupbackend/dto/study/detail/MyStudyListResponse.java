package com.studygroup.studygroupbackend.dto.study.detail;

import com.studygroup.studygroupbackend.domain.Study;
import com.studygroup.studygroupbackend.domain.StudyMember;
import com.studygroup.studygroupbackend.dto.BaseResDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MyStudyListResponse extends BaseResDto {
    private Long studyId;
    private String name;
    private String description;
    private Integer personalOrderIndex;
    private String color;

    public static MyStudyListResponse fromStudyMember(StudyMember studyMember) {
        Study study = studyMember.getStudy();

        return MyStudyListResponse.builder()
                .studyId(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .personalOrderIndex(studyMember.getPersonalOrderIndex())
                .color(study.getColor())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
