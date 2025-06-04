package com.pngo.chat_app.chat.dto;

import com.pngo.chat_app.chat.model.Message;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Integer id;

    private Integer conversationId;

    private Integer senderId;

    private String content;

    private Message.MessageType messageType;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
