package com.studygroup.studygroupbackend.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordViewController {

    private final EmailService emailService;

    @GetMapping("/password/reset")
    public String showResetForm(@RequestParam String token, Model model) {
        log.info("비밀번호 재설정 페이지 요청 (token={})", token);
        model.addAttribute("token", token);
        return "reset-password"; // → templates/reset-password.html
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<String> confirm(@ModelAttribute PasswordConfirmRequest request) {
        log.info("비밀번호 최종 변경 요청 (토큰 수신): " + request.getToken());
        try {
            emailService.confirmReset(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("비멀번호 변경 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body("유효하지 않거나 만료된 링크입니다. 다시 시도해주세요.");
        }
    }
}
