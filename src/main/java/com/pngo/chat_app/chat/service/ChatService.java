package com.pngo.chat_app.chat.service;

import com.pngo.chat_app.chat.dto.ParticipantDto;
import com.pngo.chat_app.chat.mapper.ParticipantMapper;
import com.pngo.chat_app.chat.model.Conversation;
import com.pngo.chat_app.chat.model.Message;
import com.pngo.chat_app.chat.model.Participant;
import com.pngo.chat_app.chat.repository.MessageRepository;
import com.pngo.chat_app.chat.repository.ParticipantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatService {

    ParticipantRepository participantRepository;
    ParticipantMapper participantMapper;
    MessageRepository messageRepository;

    public List<ParticipantDto> getParticipantInConversation(Integer conversationId) {
        List<Participant> conversation = participantRepository.findParticipantDtoByConversationId(conversationId);
        if (conversation != null && !conversation.isEmpty()) {

            return participantMapper.toDtoList(conversation);
        } else {
            log.warn("Conversation with id {} not found", conversationId);
            return List.of(); // Return an empty list if the conversation is not found
        }
    }

    public Message saveMessage(Message message) {
        message.setId(null);
        return messageRepository.save(message);
    }

}
