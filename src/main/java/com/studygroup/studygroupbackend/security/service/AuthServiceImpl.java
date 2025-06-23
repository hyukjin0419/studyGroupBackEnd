package com.studygroup.studygroupbackend.security.service;

import com.studygroup.studygroupbackend.dto.member.login.MemberLoginRequest;
import com.studygroup.studygroupbackend.dto.member.login.MemberLoginResponse;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateRequest;
import com.studygroup.studygroupbackend.dto.member.signup.MemberCreateResponse;
import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.security.jwt.domain.RefreshToken;
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
@Transactional
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public MemberCreateResponse createMember(MemberCreateRequest request) {
        validateDuplicateMember(request.getUserName(), request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getEmail());

        Member member = request.toEntity(encodedPassword);

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
    public void logout(String accessToken, Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);

        long expiration = jwtTokenProvider.getRemainingExpiration(accessToken);
        tokenBlacklistService.blacklistToken(accessToken,expiration);
    }

    @Override
    public RefreshTokenResponse reissueAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, member.getUserName(), member.getRole());

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }

    @Override
    public RefreshTokenResponse reissueRefreshToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 없음"));

        RefreshToken oldToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("기존 토큰 없음"));

        oldToken.expire();

        TokenWithExpiry newRefresh = jwtTokenProvider.generateRefreshToken(memberId);
        RefreshToken newRefreshEntity = RefreshToken.builder()
                .memberId(memberId)
                .token(newRefresh.getToken())
                .expiresAt(newRefresh.getExpiresAt())
                .build();

        refreshTokenRepository.save(newRefreshEntity);

        return RefreshTokenResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(memberId, member.getUserName(), member.getRole()))
                .refreshToken(newRefresh.getToken())
                .build();
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.");
        }

        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        RefreshToken tokenEntity = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("토큰 엔티티 없음"));

        if (!tokenEntity.getToken().equals(refreshToken)) {
            throw new IllegalArgumentException("일치하지 않는 토큰입니다");
        }

        if (tokenEntity.isExpired()) {
            throw new IllegalArgumentException("이미 만료된 토큰입니다.");
        }
    }
}
