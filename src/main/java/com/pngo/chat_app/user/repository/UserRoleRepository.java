package com.pngo.chat_app.user.repository;

import com.pngo.chat_app.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    // Additional query methods can be defined here if needed
    // For example, to find UserRole by userId or roleId, etc.
}
