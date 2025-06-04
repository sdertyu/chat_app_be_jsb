package com.pngo.chat_app.chat.controller;

import com.pngo.chat_app.chat.dto.ConversationDto;
import com.pngo.chat_app.chat.model.Conversation;
import com.pngo.chat_app.chat.service.ConversationService;
import com.pngo.chat_app.common.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ConversationService conversationService;

    @GetMapping("conversations")
    ApiResponse<List<ConversationDto>> getConversations(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.<List<ConversationDto>>builder()
                .message("Get all conversations successfully")
                .data(conversationService.getAllConversation(jwt))
                .build();
    }


}
