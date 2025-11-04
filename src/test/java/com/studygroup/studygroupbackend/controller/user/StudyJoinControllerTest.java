//package com.studygroup.studygroupbackend.controller.user;
//
//import com.studygroup.studygroupbackend.domain.Member;
//import com.studygroup.studygroupbackend.domain.Study;
//import com.studygroup.studygroupbackend.domain.StudyMember;
//import com.studygroup.studygroupbackend.domain.status.Role;
//import com.studygroup.studygroupbackend.domain.status.StudyRole;
//import com.studygroup.studygroupbackend.domain.status.StudyStatus;
//import com.studygroup.studygroupbackend.dto.studyJoin.leader.StudyJoinCodeResponse;
//import com.studygroup.studygroupbackend.repository.StudyMemberRepository;
//import com.studygroup.studygroupbackend.repository.StudyRepository;
//import com.studygroup.studygroupbackend.service.impl.StudyJoinServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@ExtendWith(MockitoExtension.class)
//
//class StudyJoinControllerTest {
//    @Mock
//    private StudyRepository studyRepository;
//
//    @Mock
//    private StudyMemberRepository studyMemberRepository;
//
//    @InjectMocks
//    private StudyJoinServiceImpl studyJoinService;
//
//    private Study study;
//    private Member leader;
//    private StudyMember leaderMember;
//
//    @BeforeEach
//    void setUp() {
//        leader = Member.builder()
//                .id(1L)
//                .userName("리더")
//                .password("password")
//                .email("email@email.com")
//                .role(Role.USER)
//                .build();
//
//        study = Study.builder()
//                .id(1L)
//                .name("Test Study")
//                .description("description")
//                .color("0xFF8AB4F8")
//                .dueDate(LocalDateTime.now().plusDays(30))
//                .status(StudyStatus.PROGRESSING)
//                .joinCode("abc-123")
//                .build();
//
//        leaderMember = StudyMember.of(
//                study,
//                leader,
//                study.getColor(),
//                StudyRole.LEADER,
//                0,
//
//        );
//    }
//
//    @Test
//    public void 리더가_스터디_참여코드를_정상적으로_조회한다() throws Exception{
//        //given
//        Long studyId = 1L;
//        Long leaderId = 2L;
//
//        given(studyMemberRepository.findByStudyIdAndMemberIdAndStudyRole(
//                studyId, leaderId, StudyRole.LEADER)).willReturn(Optional.of(leaderMember));
//
//        given(studyRepository.findById(studyId)).willReturn(Optional.of(study));
//
//        //when
////        StudyDetailResponse response = studyRepository.getStudyJoinCode(leaderId, studyId);
////
////        //then
////        assertThat(response).isNotNull();
////        assertThat(response.getJoinCode()).isEqualTo("abc-123");
//    }
//}