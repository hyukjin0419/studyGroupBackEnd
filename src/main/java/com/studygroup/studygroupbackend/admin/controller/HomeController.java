package com.studygroup.studygroupbackend.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 홈 컨트롤러
 * 메인 페이지 및 리디렉션 처리를 담당합니다.
 */
@Controller
public class HomeController {
    
    /**
     * 루트 경로(/) 접근 시 로그인 페이지로 리디렉션
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    /**
     * 관리자 대시보드 페이지 엔드포인트
     */
    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
