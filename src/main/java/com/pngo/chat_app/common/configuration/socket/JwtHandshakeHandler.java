package com.pngo.chat_app.common.configuration.socket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class JwtHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtDecoder jwtDecoder;

    public JwtHandshakeHandler(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String token = (String) attributes.get("jwt");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            var jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject(); // hoặc "sub"
            return () -> username;
        } catch (Exception e) {
            return null;
        }
    }
}
