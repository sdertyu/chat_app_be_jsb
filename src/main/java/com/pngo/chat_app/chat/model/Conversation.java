package com.pngo.chat_app.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pngo.chat_app.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@ToString(exclude = {"creator", "messages", "participants", "deletedConversations"})
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "title_id", length = 45)
    private String titleId;

    @Column(name = "channel_id", length = 45)
    private String channelId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ConversationType type;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User creator;

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> messages;

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Participant> participants;

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DeletedConversation> deletedConversations;

    public enum ConversationType {
        group, chat, channel
    }
}