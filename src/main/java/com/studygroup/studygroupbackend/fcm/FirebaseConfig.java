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
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

@Slf4j
@Component
public class FirebaseConfig {

    // Lightsail / EC2 서버에서 사용할 절대 경로
    private static final String SERVER_KEY_PATH = "/home/ubuntu/syncmate/sync-mate-fcm-firebase-adminsdk.json";

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials credentials;

            // ️1 서버 환경: 절대 경로에 파일이 있으면 그걸 사용
            File keyFile = new File(SERVER_KEY_PATH);
            if (keyFile.exists()) {
                log.info("Using Firebase key from server path: {}", SERVER_KEY_PATH);
                credentials = GoogleCredentials.fromStream(new FileInputStream(keyFile));

            // 2. 로컬 개발 환경: classpath(resources)에서 읽기
            } else {
                log.info("Using Firebase key from classpath: sync-mate-fcm-firebase-adminsdk.json");
                credentials = GoogleCredentials.fromStream(
                        new ClassPathResource("sync-mate-fcm-firebase-adminsdk.json").getInputStream()
                );
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            log.info("🔥 Firebase 초기화 성공");

        } catch (Exception e) {
            log.error("❌ Firebase 초기화 실패", e);
            throw new IllegalStateException("Firebase 초기화 실패", e);
        }
    }
}

