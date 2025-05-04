package org.example.schoolapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.schoolapp.enums.TokenType;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expires_at")
    private Date expiresAt;

    @Column(name = "is_expired")
    private boolean expired;

    @Column(name = "is_revoked")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
