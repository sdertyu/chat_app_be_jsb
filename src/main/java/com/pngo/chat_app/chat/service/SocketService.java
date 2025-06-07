package com.pngo.chat_app.chat.service;

import com.pngo.chat_app.chat.model.Participant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SocketService {

    ConversationService conversationService;
}
