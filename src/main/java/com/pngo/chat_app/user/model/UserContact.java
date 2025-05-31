package com.pngo.chat_app.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserContact.UserContactId.class)
public class UserContact {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "contact_id")
    private Integer contactId;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", insertable = false, updatable = false)
    private User contact;

    // Composite Key Class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserContactId implements Serializable {
        private Integer userId;
        private Integer contactId;
    }
}