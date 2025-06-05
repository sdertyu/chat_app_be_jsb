package com.pngo.chat_app.chat.controller;

import com.pngo.chat_app.chat.dto.socket.*;
import com.pngo.chat_app.chat.service.ChatService;
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
public class SocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

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
        log.warn("Received message: " + message);
        // Gửi tin nhắn đến tất cả người dùng trong cuộc trò chuyện
        messagingTemplate.convertAndSend("/topic/room/" + message.getConversationId(), message);

//        List<Integer> users = chatService.getUserInConversation(saved.getConversationId());
//        for (Integer userId : users) {
//            messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/messages", saved);
//        }
    }


}
