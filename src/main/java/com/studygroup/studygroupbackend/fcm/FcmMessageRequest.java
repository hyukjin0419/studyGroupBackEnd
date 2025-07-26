package com.studygroup.studygroupbackend.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FcmMessageRequest {
    private String fcmToken;
    private String title;
    private String body;
}
