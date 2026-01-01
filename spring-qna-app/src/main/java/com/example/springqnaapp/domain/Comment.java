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

    @Column(
            nullable = false,
            updatable = false
    )
    private String comment;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qna_id")
	@Setter(AccessLevel.PACKAGE)
	private Qna qna;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@Setter(AccessLevel.PACKAGE)
	private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Comment(String comment, User user, Qna qna) {
        this.comment = comment;
		this.user = user;
		this.qna = qna;
    }
}
