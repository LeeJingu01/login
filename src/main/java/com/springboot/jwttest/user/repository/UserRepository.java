package com.springboot.jwttest.user.repository;

import com.springboot.jwttest.user.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("""
           select u from User u
           where u.id = :login or u.email = :login
           """)
    Optional<User> findByLogin(@Param("login") String login);
}
