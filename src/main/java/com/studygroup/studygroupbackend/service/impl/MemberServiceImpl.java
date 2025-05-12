package com.studygroup.studygroupbackend.service.impl;

import com.studygroup.studygroupbackend.dto.MemberDto;
import com.studygroup.studygroupbackend.entity.Member;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public MemberDto.LoginResDto login(MemberDto.LoginReqDto request) {
        Member member = memberRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        //비밀번호 검증 (PasswordEncoder 사용)
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return MemberDto.LoginResDto.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .build();
    }

    @Override
    @Transactional
    public MemberDto.CreateResDto creatMember(MemberDto.CreateReqDto request) {
        validateDuplicateMember(request.getUserName(), request.getEmail());
        
        // 비밀번호 암호화 처리
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        // toEntity 메서드는 평문 비밀번호를 그대로 사용하므로 수정된 비밀번호로 Member 생성
        Member member = Member.of(
            request.getUserName(),
            encodedPassword,
            request.getEmail()
        );

        memberRepository.save(member);

        return MemberDto.CreateResDto
                .builder()
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
    public MemberDto.DetailResDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        return MemberDto.DetailResDto.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDto.DetailResDto updateMember(MemberDto.UpdateReqDto request) {
        Member member = memberRepository.findById(request.getId()).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        member.updateProfile(request.getUserName(),request.getEmail());

        return MemberDto.DetailResDto.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberDto.DeleteResDto deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));

        memberRepository.delete(member);

        return MemberDto.DeleteResDto.success();
    }

    @Override
    public List<MemberDto.DetailResDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto.DetailResDto::fromEntity)
                .collect(Collectors.toList());
    }
}
