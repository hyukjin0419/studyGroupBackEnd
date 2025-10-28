package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(
        value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM MEMBERS WHERE EMAIL = :email",
        nativeQuery = true
    )
    boolean existsByEmail(String email);

    @Query(
        value = "SELECT CASE WHEN COUNT (*) > 0 THEN TRUE ELSE FALSE END FROM MEMBERS WHERE USER_NAME = :userName",
        nativeQuery = true)
    boolean existsByUserName(String userName);

    Optional<Member> findByUserName(String userName);

    List<Member> findByUserNameContainingIgnoreCaseAndIdNotInOrderByUserNameAsc(String userName, Collection<Long> id);

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByIdAndDeletedFalse(Long id);
}
