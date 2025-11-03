package com.studygroup.studygroupbackend.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    //================이메일 인증================//
    @PostMapping("/send-verification-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam String email) {
        emailService.sendVerificationEmail(email);

        log.info("이메일 인증 요청 발송: {}", email);
        return ResponseEntity.ok("인증 메일이 전송되었습니다. 메일함을 확인해주세요.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            emailService.verifyEmail(token);
            log.info("이메일 인증 완료 (token validated)");
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("유효하지 않거나 만료된 토큰입니다.");
        }
    }

//    //================아이디 (userName) 찾기================//
    @PostMapping("/find-username")
    public ResponseEntity<String> findByEmail(@RequestParam String email) {
        log.info("아이디 찾기 요청: {}", email);

        emailService.sendIdRemainderEmail(email);
        return ResponseEntity.ok("인증 메일이 전송되었습니다. 메일함을 확인해주세요.");
    }
}
