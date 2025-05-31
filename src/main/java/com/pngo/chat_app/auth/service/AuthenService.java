package com.pngo.chat_app.auth.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
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

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authentication(UserLogin request) {
        log.warn("f1");
        var user = userRepository.findByEmailWithUserRoles(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENICATION);

        var roles = user.getUserRoles();
        String roleNames = roles.stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.joining(" "));


        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
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

    private String generateToken(User user) {

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
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
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

//    private String buildScope(User user) {
//        StringJoiner joiner = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty(user.getRoles()))
//        {
//            user.getRoles().forEach(joiner::add);
//        }
//        return joiner.toString();
//    }


}
