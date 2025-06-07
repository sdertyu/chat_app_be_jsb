package com.pngo.chat_app.chat.mapper;

import com.pngo.chat_app.chat.dto.socket.ChatMessage;
import com.pngo.chat_app.chat.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message toEntity(ChatMessage chatMessage);
}
