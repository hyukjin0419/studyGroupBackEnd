package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserNameOrEmail(String userName, String email);
    Optional<Member> findByUserName(String userName);
    Optional<Member> findByEmail(String email);
}
