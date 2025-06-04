package com.pngo.chat_app.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pngo.chat_app.attachment.model.Attachment;
import com.pngo.chat_app.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString(exclude = {"conversation", "sender", "attachments", "deletedMessages", "lastReadByParticipants"})
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "conversation_id")
    private Integer conversationId;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType messageType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @JsonIgnore
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Attachment> attachments;

    @JsonIgnore
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DeletedMessage> deletedMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "lastReadMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Participant> lastReadByParticipants;

    public enum MessageType {
        text, image, file
    }
}