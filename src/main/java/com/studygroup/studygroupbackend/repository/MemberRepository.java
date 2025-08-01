package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUserNameOrEmail(String userName, String email);
    Optional<Member> findByUserName(String userName);
    Optional<Member> findByEmail(String userEmail);

    List<Member> findByUserNameContainingIgnoreCaseAndIdNotInOrderByUserNameAsc(String userName, Collection<Long> id);

    Optional<Member> findByUuid(String uuid);
}
