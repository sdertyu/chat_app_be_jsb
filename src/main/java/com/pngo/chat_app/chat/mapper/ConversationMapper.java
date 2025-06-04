package com.pngo.chat_app.chat.mapper;

import com.pngo.chat_app.chat.dto.ConversationDto;
import com.pngo.chat_app.chat.model.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
//        uses = {ParticipantMapper.class, MessageMapper.class}
)
public interface ConversationMapper {

//    @Mapping(source = "participants", target = "participants")
//    @Mapping(source = "messages", target = "messages")
//    ConversationDto toDto(Conversation conversation);


    List<ConversationDto> toDtoList(List<Conversation> conversations);
}
