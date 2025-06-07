package com.pngo.chat_app.chat.repository;

import com.pngo.chat_app.chat.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    @Query("""
                SELECT p FROM Participant p JOIN FETCH p.user u
                WHERE p.conversation.id = :conversationId
            """)
    List<Participant> findParticipantDtoByConversationId(@Param("conversationId") Integer conversationId);
}
