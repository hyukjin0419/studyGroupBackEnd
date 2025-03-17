package com.studygroup.studygroupbackend.service;

import com.studygroup.studygroupbackend.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto.LoginResDto login(MemberDto.LoginReqDto request);
    MemberDto.CreateResDto creatMember(MemberDto.CreateReqDto request);
    MemberDto.DetailResDto getMemberById(Long id);
    MemberDto.UpdateResDto updateMember(MemberDto.UpdateReqDto request);
    MemberDto.DeleteResDto deleteMember(Long id);
}
