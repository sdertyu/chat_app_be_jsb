package com.pngo.chat_app.chat.dto.socket;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {
    int id;
    int senderId;
    String content;
    int conversationId;
    LocalDateTime createdAt;
}
