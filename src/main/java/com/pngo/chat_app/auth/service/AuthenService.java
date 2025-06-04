package com.pngo.chat_app.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pngo.chat_app.auth.model.RefreshToken;
import com.pngo.chat_app.common.configuration.CookieBearerTokenResolver;
import com.pngo.chat_app.user.mapper.UserMapper;
import com.pngo.chat_app.user.dto.request.UserLogin;
import com.pngo.chat_app.common.dto.request.IntrospectRequest;
import com.pngo.chat_app.user.dto.request.UserSignup;
import com.pngo.chat_app.auth.dto.response.AuthenticationResponse;
import com.pngo.chat_app.common.dto.response.IntrospectResponse;
import com.pngo.chat_app.user.model.User;
import com.pngo.chat_app.common.exception.AppException;
import com.pngo.chat_app.common.enums.ErrorCode;
import com.pngo.chat_app.user.repository.UserRepository;
import com.pngo.chat_app.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenService {
    UserService userService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.keyTimeToLive}")
    protected Integer KEY_TIME_TO_LIVE;

    @NonFinal
    @Value("${jwt.keyTimeRefresh}")
    protected Integer KEY_TIME_REFRESH;

    public AuthenticationResponse authentication(UserLogin request) {
        User user = userRepository.findByEmailWithUserRoles(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENICATION);

        var roles = user.getUserRoles();
        String roleNames = roles.stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.joining(" "));


        String token = generateToken(user, KEY_TIME_TO_LIVE);
        String rfToken = generateToken(user, KEY_TIME_REFRESH);

        RefreshToken refreshToken = refreshTokenService.findRefreshTokenNotExpires(user.getId());
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .userId(user.getId())
                    .token(rfToken)
                    .expiresAt(LocalDateTime.now().plusDays(KEY_TIME_REFRESH))
                    .ipAddress("")
                    .userAgent("")
                    .revoked(false)
                    .build();
        } else {
            refreshToken.setToken(rfToken);
            refreshToken.setExpiresAt(LocalDateTime.now().plusDays(KEY_TIME_REFRESH));
        }

        log.warn(refreshToken.toString());

        refreshTokenService.saveRefreshToken(refreshToken);

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(rfToken)
                .user(userMapper.userToUserResponse(user))
                .authenticated(true)
                .build();

    }

    public boolean signup(UserSignup request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        userService.saveUser(request);

        return true;
    }

    private String generateToken(User user, Integer time) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        log.info(user.toString());
        List<String> roleNames = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .toList();


        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("chat_app")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(time, ChronoUnit.DAYS).toEpochMilli()
                ))
                .claim("scope", String.join(" ", roleNames))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error generating token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspectRequest(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean check = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(check && expirationTime.after(new Date()))
                .build();

    }

    public boolean verifyToken(HttpServletRequest request) {
        try {
            CookieBearerTokenResolver cookieBearerTokenResolver = new CookieBearerTokenResolver();
            String token = cookieBearerTokenResolver.resolve(request);
            if (token == null) {
                return false;
            }

            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify signature
            boolean validSignature = signedJWT.verify(verifier);

            // Check expiration
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean notExpired = expirationTime != null && expirationTime.after(new Date());

            return validSignature && notExpired;
        } catch (ParseException | JOSEException e) {
            log.error("Error verifying token", e);
            return false;
        }
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        try {
            CookieBearerTokenResolver cookieBearerTokenResolver = new CookieBearerTokenResolver();
            String token = cookieBearerTokenResolver.resolveRefreshToken(request);
            if (token == null) {
                throw new AppException(ErrorCode.UNAUTHENICATION);
            }
            SignedJWT signedJWT = SignedJWT.parse(token);

            User user = userRepository.findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

            RefreshToken refreshToken = refreshTokenService.findRefreshTokenNotExpires(user.getId());

            String newToken = "";
            if (token.equals(refreshToken.getToken())) {
                if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                    throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
                } else {
                    newToken = generateToken(user, KEY_TIME_TO_LIVE);
                }
            }


            return AuthenticationResponse.builder()
                    .token(newToken)
                    .user(userMapper.userToUserResponse(user))
                    .authenticated(true)
                    .build();

        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new AppException(ErrorCode.UNAUTHENICATION);
        }
    }

//    private String buildScope(User user) {
//        StringJoiner joiner = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty(user.getRoles()))
//        {
//            user.getRoles().forEach(joiner::add);
//        }
//        return joiner.toString();
//    }


}
