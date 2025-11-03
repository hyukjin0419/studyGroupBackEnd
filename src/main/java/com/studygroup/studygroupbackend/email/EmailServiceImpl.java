package com.studygroup.studygroupbackend.email;

import com.studygroup.studygroupbackend.domain.Member;
import com.studygroup.studygroupbackend.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
//TODO 추후 중간 인터페이스 추가
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;

    @Value("${jwt.verification.secret}")
    private String secretKey;

    @Value("${app.base-url}")
    private String baseUrl;

    private final long verificationExpiration = 1000L * 60 * 5;

    //================이메일 인증================//
    @Override
    public void sendVerificationEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 회원을 찾을 수 없습니다."));

        String token = generateVerificationToken(member.getEmail());
        String verificationUrl = baseUrl + "/auth/email/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("[SyncMate] 이메일 인증 요청");
        message.setText("안녕하세요 [SynMate] 인증 이메일입니다.\n 아래 링크를 클릭하여 이메일 인증을 완료해주세요:\n\n" + verificationUrl);
        mailSender.send(message);
    }

    @Override
    public void verifyEmail(String token) {
        String email = validateAndExtractEmail(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다"));
        member.verifyEmail();
        memberRepository.save(member);
    }


    private String generateVerificationToken(String email) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new Date(System.currentTimeMillis() + verificationExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String validateAndExtractEmail(String token) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰입니다.");
        }
    }

    //================아이디 (userName) 찾기================//

    @Override
    public void sendIdRemainderEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 회원을 찾을 수 없습니다."));

        String subject = "[SyncMate] 아이디 안내";

        String content = """
                안녕하세요, SyncMate입니다.
                
                회원님의 아이디는 다음과 같습니다.
        
                아이디: %s
                
                안전한 사용을 위해 비밀번호는 타인과 공유하지 마세요.
                감사합니다.
                """.formatted(member.getUserName());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
        log.info("✅ 아이디 안내 메일 발송 완료: {}", member.getEmail());
    }
}
