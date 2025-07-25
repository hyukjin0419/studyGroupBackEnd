package com.studygroup.studygroupbackend.fcm;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FcmController", description = "FCM용 컨트롤러")
@Slf4j
@RestController
//@RequestMapping("/fcm")
@RequestMapping("/auth/fcm") //test시 로그인 없이 사용할 수 있도록
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("send")
    public ResponseEntity<String> sendTest(@RequestBody FcmMessageRequest request) {
        log.info("여기 들어옴");
        fcmService.sendFcmMessage(request);
        return ResponseEntity.ok("푸시 발송 완료");
    }
}

/*
팀장이 팀원에게 초대를 한다
초대 메세지가 발송된다
팀원이 초대메세지를 받는다
초대 메세지를 클릭한다
초대 된다.

--------------------
1. 우선 login시 device token을 추가해야한다 (중복 처리 포함)
2. 팀장 studyinvite api 생성
- study_invitation에 초대 기록 생성
- notification에 알림 저장
- device_token에서 해당 멤버의 모든 기기를 fcm 토큰으로 조회 (근데 현재 Refresh token 하나라서 기기 하나밖에 발송 안되긴 함)
- payload에 invitaitonID 포함 ? -> 뭐를 포함해야 하지?
3. 팀원이 message 수신
- 푸시 클릭 시 payload 기반으로 라우팅
- flutter에서 서버로 get /inviations/{inviataionId}요청..?
4. 팀원이 초대 수락
- study_member에 새로운 row 생성
- study_inviation의 status를 ACCEPTED, responded_at 기록
- notification에 "초대 수락됨" 메시지 추가 (선택)
- 프론트에서 핻아 스터디 화면으로 이동
4.1 거절 시
- study_inviation의 status를 REJECTED, responded_at 기록
 */