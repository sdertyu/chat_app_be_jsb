package com.pngo.chat_app.chat.controller;

import com.pngo.chat_app.chat.dto.socket.*;
import com.pngo.chat_app.chat.mapper.MessageMapper;
import com.pngo.chat_app.chat.service.ChatService;
import com.pngo.chat_app.chat.service.ConversationService;
import com.pngo.chat_app.chat.service.SocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketController {


    SimpMessagingTemplate messagingTemplate;

    ChatService chatService;
    ConversationService conversationService;

    MessageMapper messageMapper;

    @MessageMapping("/typing")
    public void handleTyping(TypingPayload payload) {
        messagingTemplate.convertAndSend("/topic/" + payload.getConversationId(), payload);
    }

    @MessageMapping("/join")
    public void handleJoin(JoinPayload payload, Principal principal) {
        log.warn(principal.getName());
    }

    @MessageMapping("/leave")
    public void handleLeave(LeavePayload payload, Principal principal) {
        // Không cần xử lý server-side, client tự leave
    }

    @MessageMapping("/read")
    public void handleRead(ReadMessagePayload payload) {
//        chatService.readMessage(payload.getConversationId(), payload.getLastMessageId(), payload.getUserId());
        messagingTemplate.convertAndSend("/topic/" + payload.getConversationId(), payload);
    }

    @MessageMapping("/join-user-room")
    public void handleJoinUserRoom(Integer userId, Principal principal) {
        // Không cần xử lý server-side, client tự join
        // Có thể dùng để gửi thông báo đến người dùng cụ thể
//        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", "User " + userId + " has joined the room.");

    }

    @MessageMapping("/chat.send")
    public void handleSendMessage(@Payload ChatMessage message, Principal principal) {
//        ChatMessage saved = chatService.saveMessage(message);
        Integer conversationId = message.getConversationId();

        var newMessage = chatService.saveMessage(messageMapper.toEntity(message));

        message.setId(newMessage.getId());
        message.setCreatedAt(newMessage.getCreatedAt());

        var participants = chatService.getParticipantInConversation(conversationId);
//        System.out.println("Participants in conversation " + conversationId + ": " + participants);
        if (participants.isEmpty()) {
            log.warn("No participants found for conversation {}", conversationId);
            return;
        }
        log.info(principal.getName());
        participants.forEach(
                p -> {
                    log.warn(p.getUser().getEmail());
                    messagingTemplate.convertAndSendToUser(
                            p.getUser().getEmail(),
                            "/queue/notifications",
                            message
                    );
                }

        );


    }


    @MessageMapping("/chat.typing")
    public void handleTyping(TypingPayload payload, Principal principal) {
        messagingTemplate.convertAndSend("/topic/room/" + payload.getConversationId(), payload);
    }
}
