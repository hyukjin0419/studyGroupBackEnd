package com.studygroup.studygroupbackend.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@Component
public class FirebaseConfig {

    // Railway Variables에 넣을 값 (Base64 인코딩된 JSON)
    @Value("${FIREBASE_CREDENTIALS_BASE64:}")
    private String credsB64;

    @PostConstruct
    public void initialize() {
        try {
            var credentials = StringUtils.hasText(credsB64)
                    ? GoogleCredentials.fromStream(new ByteArrayInputStream(Base64.getDecoder().decode(credsB64)))
                    // dev만: resources에 둔 파일(깃에 커밋 금지)
                    : GoogleCredentials.fromStream(new ClassPathResource("sync-mate-fcm-firebase-adminsdk.json").getInputStream());

            var options = FirebaseOptions.builder().setCredentials(credentials).build();
            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);

            log.info("Firebase 초기화 성공");
        } catch (Exception e) {
            throw new IllegalStateException("Firebase 초기화 실패: ", e);
        }
    }
}


