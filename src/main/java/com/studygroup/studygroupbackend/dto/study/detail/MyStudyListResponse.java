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
//전체 팀 화면에 노출 시킬 녀석들
public class MyStudyListResponse extends BaseResDto {
    private Long studyId;
    private String name;
    private String description;
    private Integer personalOrderIndex;
    private String personalColor;

    public static MyStudyListResponse fromStudyMember(StudyMember studyMember) {
        Study study = studyMember.getStudy();

        return MyStudyListResponse.builder()
                .studyId(study.getId())
                .name(study.getName())
                .description(study.getDescription())
                .personalOrderIndex(studyMember.getPersonalOrderIndex())
                .personalColor(studyMember.getPersonalColor())
                .createdAt(study.getCreatedAt())
                .modifiedAt(study.getModifiedAt())
                .build();
    }
}
