package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
    /**
     * 특정 회원이 리더인 모든 스터디 조회
     * 
     * @param leader 리더 회원 엔티티
     * @return 해당 리더의 스터디 목록
     */
    List<Study> findByLeader_Id(Long leaderId);
}
