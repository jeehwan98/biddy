package com.jee.back.user.repository;

import com.jee.back.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    User findByUserId(String userId);
}
