package com.example.springqnaapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qna_id")
    @Setter(AccessLevel.PACKAGE)
    private Qna qna;

    @Column(
            nullable = false,
            updatable = false
    )
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Comment(String comment) {
        this.comment = comment;
    }

	public User getUser() {
		return null;
	}
}
