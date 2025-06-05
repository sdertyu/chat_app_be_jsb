package com.pngo.chat_app.chat.dto.socket;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingPayload {
    String conversationId;
    int senderId;
}
