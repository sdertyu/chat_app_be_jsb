package com.pngo.chat_app.configuration;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import jakarta.servlet.http.HttpServletRequest;

public class CookieBearerTokenResolver implements BearerTokenResolver {
    @Override
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}