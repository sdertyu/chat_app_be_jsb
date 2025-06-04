package com.pngo.chat_app.auth.repository;

import com.pngo.chat_app.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    RefreshToken findByUserIdAndRevokedFalse(Integer userId);
}
