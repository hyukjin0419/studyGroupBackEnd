package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateResponse;

import java.util.List;

public interface StudyService {
    StudyCreateResponse createStudy(StudyCreateRequest request);
    StudyDetailResponse getStudyById(Long studyId);
    List<StudyListResponse> getAllStudies();
    StudyUpdateResponse updateStudy(Long studyId, Long leaderId, StudyUpdateRequest request);
    StudyDeleteResponse deleteStudy(Long studyId, Long leaderId);

}
