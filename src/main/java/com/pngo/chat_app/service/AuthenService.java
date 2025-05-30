package com.pngo.chat_app.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pngo.chat_app.dto.request.AuthenticationRequest;
import com.pngo.chat_app.dto.request.IntrospectRequest;
import com.pngo.chat_app.dto.response.AuthenticationResponse;
import com.pngo.chat_app.dto.response.IntrospectResponse;
import com.pngo.chat_app.entity.User;
import com.pngo.chat_app.exception.AppException;
import com.pngo.chat_app.exception.ErrorCode;
import com.pngo.chat_app.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenService {
    UserService userService;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenicated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenicated)
            throw new AppException(ErrorCode.UNAUTHENICATION);

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenicated(true)
                .build();

    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("chat_app")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", user.getRoles())
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

//    private String buildScope(User user) {
//        StringJoiner joiner = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty(user.getRoles()))
//        {
//            user.getRoles().forEach(joiner::add);
//        }
//        return joiner.toString();
//    }


}
