package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.study.create.StudyCreateRequest;
import com.studygroup.studygroupbackend.dto.study.create.StudyCreateResponse;
import com.studygroup.studygroupbackend.dto.study.delete.StudyDeleteResponse;
import com.studygroup.studygroupbackend.dto.study.detail.MyStudyListResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyDetailResponse;
import com.studygroup.studygroupbackend.dto.study.detail.StudyListResponse;
import com.studygroup.studygroupbackend.dto.study.update.StudyOrderUpdateListRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyOrderUpdateRequest;
import com.studygroup.studygroupbackend.dto.study.update.StudyUpdateRequest;

import java.util.List;

public interface StudyService {
    StudyCreateResponse createStudy(Long leaderId, StudyCreateRequest request);
    StudyDetailResponse getStudyById(Long studyId);
    List<StudyListResponse> getAllStudies();
    List<StudyListResponse> getStudiesByMemberId(Long memberId);
    List<MyStudyListResponse> getStudiesByMemberIdAsc(Long memberId);
    void updateStudyOrder(Long memberId, List<StudyOrderUpdateRequest> requests);
    StudyDetailResponse updateStudy(Long studyId, Long leaderId, StudyUpdateRequest request);
    StudyDeleteResponse deleteStudy(Long studyId, Long leaderId);

}
