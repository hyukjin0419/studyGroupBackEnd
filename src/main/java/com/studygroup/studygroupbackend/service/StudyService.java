package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.StudyDto;

import java.util.List;

public interface StudyService {
    StudyDto.CreateResDto createStudy(StudyDto.CreateReqDto request);
    StudyDto.DetailResDto getStudyById(Long studyId);
    List<StudyDto.ListResDto> getAllStudies();
    StudyDto.UpdateResDto updateStudy(Long studyId, Long leaderId, StudyDto.UpdateReqDto request);
    StudyDto.DeleteResDto deleteStudy(Long studyId, Long leaderId);

}
