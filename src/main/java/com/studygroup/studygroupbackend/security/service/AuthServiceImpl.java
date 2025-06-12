package com.studygroup.studygroupbackend.security.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;
import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.security.jwt.entity.RefreshToken;
import com.studygroup.studygroupbackend.security.jwt.JwtTokenProvider;
import com.studygroup.studygroupbackend.security.jwt.dto.RefreshTokenResponse;
import com.studygroup.studygroupbackend.security.jwt.dto.TokenWithExpiry;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import com.studygroup.studygroupbackend.security.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest request) {
        validateDuplicateMember(request.getUserName(), request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getEmail());

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
    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getId(),member.getUserName(), member.getRole());

        TokenWithExpiry refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .memberId(member.getId())
                .token(refreshToken.getToken())
                .expiresAt(refreshToken.getExpiresAt())
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return MemberLoginResponse.builder()
                .id(member.getId())
                .userName(member.getUserName())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public RefreshTokenResponse reissueAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
        }

        Long memberId = jwtTokenProvider.getMemberId(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("저장된 리프레시 토큰이 없습니다."));

        if(!savedToken.getToken().equals(refreshToken)){
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId,member.getUserName(),member.getRole());

        return RefreshTokenResponse.builder().
                accessToken(newAccessToken)
                .build();
    }

    @Override
    public void logout(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
