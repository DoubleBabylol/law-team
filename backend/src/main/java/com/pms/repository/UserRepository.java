package com.pms.repository;

import com.pms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByClerkNo(String clerkNo);

    boolean existsByUsername(String username);

    boolean existsByClerkNo(String clerkNo);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.clerkNo LIKE %:keyword%")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
