package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.update.MemberUpdateRequest;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberDetailResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        return MemberDetailResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDetailResponse updateMember(MemberUpdateRequest request) {
        Member member = memberRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        member.updateProfile(request.getUserName(),request.getEmail());

        return MemberDetailResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDeleteResponse deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));

        memberRepository.delete(member);

        return MemberDeleteResponse.success();
    }

    @Override
    public List<MemberDetailResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDetailResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
