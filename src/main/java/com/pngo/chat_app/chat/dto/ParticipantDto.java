package com.pngo.chat_app.chat.dto;

import com.pngo.chat_app.chat.model.Participant;
import com.pngo.chat_app.user.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipantDto {

    private Integer id;

    private Integer conversationId;

    private Integer usersId;

    private Participant.ParticipantType type;

    private Integer lastReadMessageId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private User user;
}
