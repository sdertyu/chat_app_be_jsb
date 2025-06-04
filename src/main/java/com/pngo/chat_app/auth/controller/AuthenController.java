package com.pngo.chat_app.auth.controller;


import com.nimbusds.jose.JOSEException;
import com.pngo.chat_app.common.dto.response.ApiResponse;
import com.pngo.chat_app.user.dto.request.UserLogin;
import com.pngo.chat_app.common.dto.request.IntrospectRequest;
import com.pngo.chat_app.user.dto.request.UserSignup;
import com.pngo.chat_app.auth.dto.response.AuthenticationResponse;
import com.pngo.chat_app.common.dto.response.IntrospectResponse;
import com.pngo.chat_app.auth.service.AuthenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenController {

    AuthenService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody UserLogin request, HttpServletResponse response) {
        AuthenticationResponse result = authenticationService.authentication(request);

        // Set JWT as HttpOnly cookie
        Cookie cookie = new Cookie("jwt", result.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1 hour

        Cookie refreshCookie = new Cookie("rf_tkn", result.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days


        response.addCookie(cookie);
        response.addCookie(refreshCookie);
        // Set authentication status
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successful")
                .data(result)
                .build();
    }

    @PostMapping("/signup")
    ApiResponse<Boolean> signup(@RequestBody @Valid UserSignup request) {
        boolean result = authenticationService.signup(request);

        return ApiResponse.<Boolean>builder()
                .message("Signup successful")
                .data(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        IntrospectResponse result = authenticationService.introspectRequest(request);

        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @GetMapping("verify-token")
    ApiResponse<Boolean> verifyToken(HttpServletRequest request) {
        boolean isValid = authenticationService.verifyToken(request);
        return ApiResponse.<Boolean>builder()
                .data(isValid)
                .build();
    }

    @PostMapping("/refresh-token")
    ApiResponse<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse result = authenticationService.refreshToken(request);

        if(!result.getToken().isEmpty()){
            Cookie cookie = new Cookie("jwt", result.getToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1 day
            response.addCookie(cookie);
        }
        // Set new JWT as HttpOnly cookie


        return ApiResponse.<AuthenticationResponse>builder()
                .message("Token refreshed successfully")
                .data(result)
                .build();
    }
}
