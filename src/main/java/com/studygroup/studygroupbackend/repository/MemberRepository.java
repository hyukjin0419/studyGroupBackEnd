package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    Optional<Member> findByUserName(String userName);

    List<Member> findByUserNameContainingIgnoreCaseAndIdNotInOrderByUserNameAsc(String userName, Collection<Long> id);

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByIdAndDeletedFalse(Long id);
}
