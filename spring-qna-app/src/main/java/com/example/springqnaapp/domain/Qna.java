package com.example.springqnaapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "qnas")
@Getter
@NoArgsConstructor
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Setter(AccessLevel.PACKAGE)
    private User user;

    @Column(
            length = 100,
            nullable = false,
            updatable = false
    )
    private String title;

    @OneToMany(
            mappedBy = "qna",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Qna(User user, String title) {
        this.user = user;
        this.title = title;
    }

    public Comment addComment(Comment comment) {
        comments.add(comment);
        comment.setQna(this);
		return comment;
    }

	public boolean isWriter(String username) {
		return user.getUsername().equals(username);
	}

	public boolean commentable(String username) {
		return comments.getLast().getUser().getUsername().equals(username);
	}
}
