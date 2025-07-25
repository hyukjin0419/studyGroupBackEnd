package com.studygroup.studygroupbackend.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmService {

    public void sendFcmMessage(FcmMessageRequest request){
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Message message = Message.builder()
                .setToken(request.getFcmToken())
                .setNotification(notification)
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
