package com.studygroup.studygroupbackend.scheduler;

import com.studygroup.studygroupbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletionScheduler {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyInvitationRepository studyInvitationRepository;
    private final ChecklistItemRepository checklistItemRepository;


    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    @Transactional
    public void cleanUp() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);

        int memberCount = memberRepository.deleteExpired(threshold);
        int studyCount = studyRepository.deleteExpired(threshold);
        int studyMemberCount = studyMemberRepository.deleteExpired(threshold);
        int checklistItemCount = checklistItemRepository.deleteExpired(threshold);
        int studyInvitationCount = studyInvitationRepository.deleteExpired(threshold);

        log.info("[SCHEDULER][DELETE] Member={}, Study={}, StudyMember={}, StudyInvitation={}, Item={}",
                memberCount, studyCount, studyMemberCount, studyInvitationCount, checklistItemCount);
    }

//    @Scheduled(cron = "*/10 * * * * *") // 매 10초
//    @Transactional
//    public void test() {
//        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
//
//        int checklistItemCount = checklistItemRepository.deleteExpired(threshold);
//
//        log.info("[SCHEDULER][DELETE] Item={}", checklistItemCount);
//    }
}
