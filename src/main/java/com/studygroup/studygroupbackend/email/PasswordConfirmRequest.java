package com.studygroup.studygroupbackend.email;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordConfirmRequest {
    private String token;
    private String newPassword;
}
