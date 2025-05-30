package com.pngo.chat_app.controller;


import com.nimbusds.jose.JOSEException;
import com.pngo.chat_app.dto.request.ApiResponse;
import com.pngo.chat_app.dto.request.AuthenticationRequest;
import com.pngo.chat_app.dto.request.IntrospectRequest;
import com.pngo.chat_app.dto.response.AuthenticationResponse;
import com.pngo.chat_app.dto.response.IntrospectResponse;
import com.pngo.chat_app.service.AuthenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenController {

    AuthenService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse result = authenticationService.authentication(request);

        // Set JWT as HttpOnly cookie
        Cookie cookie = new Cookie("jwt", result.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour
        response.addCookie(cookie);

        // Set authentication status
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Login successful")
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
}
