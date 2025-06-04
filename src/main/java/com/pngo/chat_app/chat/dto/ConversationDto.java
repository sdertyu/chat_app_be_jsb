package com.pngo.chat_app.chat.dto;

import com.pngo.chat_app.chat.model.Message;
import com.pngo.chat_app.chat.model.Participant;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ConversationDto {

    private Integer id;
    private String name;
    private String titleId;
    private String channelId;
    private Integer createdBy;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<ParticipantDto> participants;
    private Set<MessageDto> messages;
}
