package com.pngo.chat_app.auth.service;

import com.pngo.chat_app.auth.model.RefreshToken;
import com.pngo.chat_app.auth.repository.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshTokenNotExpires(Integer userId) {
        return refreshTokenRepository.findByUserIdAndRevokedFalse(userId);
    }

}
