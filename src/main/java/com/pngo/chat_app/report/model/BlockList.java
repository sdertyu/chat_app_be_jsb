package com.pngo.chat_app.report.model;

import com.pngo.chat_app.chat.model.Participant;
import com.pngo.chat_app.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "block_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "users_id")
    private Integer usersId;

    @Column(name = "participants_id")
    private Integer participantsId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participants_id", insertable = false, updatable = false)
    private Participant participant;
}