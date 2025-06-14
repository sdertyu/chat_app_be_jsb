package com.pngo.chat_app.chat.model;

import com.pngo.chat_app.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "deleted_conversations")
@Getter
@Setter
@ToString(exclude = {"conversation", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class DeletedConversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "conversation_id")
    private Integer conversationId;

    @Column(name = "users_id")
    private Integer usersId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private User user;
}