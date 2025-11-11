package com.studygroup.studygroupbackend.repository;

import com.studygroup.studygroupbackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.userName = :userName")
    boolean existsByUserName(@Param("userName") String userName);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.email = :email")
    boolean existsByEmail(@Param("email") String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserName(String userName);

    List<Member> findByUserNameContainingIgnoreCaseAndIdNotInOrderByUserNameAsc(String userName, Collection<Long> id);

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByIdAndDeletedFalse(Long id);
}
