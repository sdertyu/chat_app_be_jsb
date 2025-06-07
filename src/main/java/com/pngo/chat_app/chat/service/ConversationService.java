package com.pngo.chat_app.chat.service;

import com.pngo.chat_app.chat.dto.ConversationDto;
import com.pngo.chat_app.chat.dto.ParticipantDto;
import com.pngo.chat_app.chat.mapper.ConversationMapper;
import com.pngo.chat_app.chat.mapper.ParticipantMapper;
import com.pngo.chat_app.chat.model.Conversation;
import com.pngo.chat_app.chat.model.Participant;
import com.pngo.chat_app.chat.repository.ConversationRepository;
import com.pngo.chat_app.user.model.User;
import com.pngo.chat_app.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ConversationService {

    ConversationRepository conversationRepository;
    UserService userService;
    ConversationMapper conversationMapper;
    ParticipantMapper participantMapper;

    @Transactional
    public List<ConversationDto> getAllConversation(Jwt jwt) {
        String email = jwt.getSubject();
        Optional<User> user = userService.getUserByEmail(email);

        var list = conversationMapper.toDtoList(conversationRepository.findByParticipantsId(user.get().getId()));


        return list;
    }

}
