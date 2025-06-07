package com.pngo.chat_app.chat.mapper;

import com.pngo.chat_app.chat.dto.ParticipantDto;
import com.pngo.chat_app.chat.model.Participant;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = "spring"
//        uses = {ParticipantMapper.class, MessageMapper.class}
)
public interface ParticipantMapper {

    List<ParticipantDto> toDtoList(List<Participant> participants);
}
