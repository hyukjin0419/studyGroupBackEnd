package com.studygroup.studygroupbackend.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmMessageRequest {
    private String fcmToken;
    private String title;
    private String body;
}
