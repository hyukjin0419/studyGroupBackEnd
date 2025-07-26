package com.studygroup.studygroupbackend.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FcmInvitationRequest extends FcmMessageRequest {
    private Long invitationId;

    public static FcmInvitationRequest of(
            String fcmToken,
            String title,
            String body,
            Long invitationId
    ) {
        return FcmInvitationRequest.builder()
                .fcmToken(fcmToken)
                .title(title)
                .body(body)
                .invitationId(invitationId)
                .build();
    }
}
