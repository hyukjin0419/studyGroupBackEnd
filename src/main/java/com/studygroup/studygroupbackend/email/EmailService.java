package com.studygroup.studygroupbackend.email;

import com.studygroup.studygroupbackend.domain.Member;

public interface EmailService {
    void sendVerificationEmail(String email);
    void verifyEmail(String token) ;
    void sendIdRemainderEmail(String email);
    void requestReset(String email);
    void confirmReset(String email, String newPassword);
}
