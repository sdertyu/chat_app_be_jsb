package com.pngo.chat_app.chat.repository;

import com.pngo.chat_app.chat.model.Conversation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("SELECT DISTINCT  c FROM Conversation c " +
            "JOIN c.participants p " +
            "JOIN FETCH c.messages m " +
            "WHERE p.user.id = :id "
    )
    List<Conversation> findByParticipantsId(@Param("id") Integer id);


    @Query("SELECT c FROM Conversation c JOIN FETCH c.participants WHERE c.id = :id")
    Optional<Conversation> findByIds(@Param("id") Integer id);


}
