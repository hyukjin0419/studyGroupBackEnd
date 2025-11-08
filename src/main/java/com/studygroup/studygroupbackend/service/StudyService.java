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
    StudyDetailResponse getStudyByMemberIdAndStudyId(Long memberId, Long studyId);
    List<StudyDetailResponse> getStudiesByMemberIdAsc(Long memberId);
    StudyDetailResponse updateStudy(Long leaderId, StudyUpdateRequest request);
    void updateStudyOrder(Long memberId, List<StudyOrderUpdateRequest> requests);
    StudyDeleteResponse deleteStudy(Long studyId, Long leaderId);
    void leaveStudy(Long studyId, Long memberId);
}
