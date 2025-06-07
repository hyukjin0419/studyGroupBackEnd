package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.member.MemberDto;
import com.studygroup.studygroupbackend.dto.member.delete.MemberDeleteResponse;
import com.studygroup.studygroupbackend.dto.member.detail.MemberDetailResponse;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;
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
    @Transactional
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        //비밀번호 검증 (단순 비교, 보안 없음)
        if (!request.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return MemberLoginResponse.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .build();
    }

    @Override
    @Transactional
    public MemberCreateResponse creatMember(MemberCreateRequest request) {
        validateDuplicateMember(request.getUserName(), request.getEmail());
        Member member = request.toEntity();

        memberRepository.save(member);

        return MemberCreateResponse.builder()
                .id(member.getId())
                .build();
    }

    private void validateDuplicateMember(String userName, String email) {
        boolean exists = memberRepository.existsByUserNameOrEmail(userName, email);
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }

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
