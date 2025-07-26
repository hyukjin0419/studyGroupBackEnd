package com.studygroup.studygroupbackend.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    public void sendInvitationPush(FcmInvitationRequest request) {
        try{
            Map<String, String> data = new HashMap<>();
            data.put("type", "INVITATION");
            data.put("InvitationId", String.valueOf(request.getInvitationId()));

            sendFcmMessage(request, data);
        } catch (Exception e){
            log.info("FCM 초대  메세지 전송 실패", e);
        }
    }

    public void sendFcmMessage(FcmMessageRequest request, Map<String, String> data){
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Message message = Message.builder()
                .setToken(request.getFcmToken())
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("푸시 메시지 전송 성공: " + response);
        } catch (FirebaseMessagingException e){
            log.info("푸시 메세지 전송 실패");
            e.printStackTrace();
        }
    }
}
