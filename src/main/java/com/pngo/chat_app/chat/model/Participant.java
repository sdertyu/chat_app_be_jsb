package com.pngo.chat_app.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pngo.chat_app.report.model.BlockList;
import com.pngo.chat_app.report.model.Report;
import com.pngo.chat_app.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "participants")
@Getter
@Setter
@ToString(exclude = {"conversation", "user", "lastReadMessage", "blockedBy", "reportedBy"})
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "conversation_id")
    private Integer conversationId;

    @Column(name = "users_id")
    private Integer usersId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ParticipantType type;

    @Column(name = "lastReadMessageId")
    private Integer lastReadMessageId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false)
    private Conversation conversation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastReadMessageId", insertable = false, updatable = false)
    private Message lastReadMessage;

    @JsonIgnore
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BlockList> blockLists;

    @JsonIgnore
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Report> reports;

    public enum ParticipantType {
        admin, member
    }
}