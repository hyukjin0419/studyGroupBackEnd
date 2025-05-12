package com.studygroup.studygroupbackend.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller to handle login page requests
 */
@Controller
public class LoginController {

    /**
     * 로그인 페이지 요청 처리
     * /login URL로 접근 시 static/login.html을 반환
     * 
     * @return login 페이지 경로
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // .html 확장자 제거 - Thymeleaf가 자동으로 확장자를 추가
    }
}
