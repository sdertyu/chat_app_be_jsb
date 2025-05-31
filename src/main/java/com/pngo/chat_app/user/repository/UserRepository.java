package com.pngo.chat_app.user.repository;

import com.pngo.chat_app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository()
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE u.email = :email")
    Optional<User> findByEmailWithUserRoles(@Param("email") String email);


//    boolean existsByUsername(String username);
//
//    boolean existsByEmail(String email);
}
