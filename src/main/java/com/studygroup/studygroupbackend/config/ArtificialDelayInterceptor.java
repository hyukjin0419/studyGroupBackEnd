package com.studygroup.studygroupbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component // 필터 실행 순서 (낮을수록 먼저 실행)
public class ArtificialDelayInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("모든 controller에 딜레이");
        Thread.sleep(1000); // 0.3초 딜레이
        return true; // 다음 단계 진행
    }
}
