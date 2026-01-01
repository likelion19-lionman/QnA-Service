package com.example.springqnaapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

	public boolean accessible(String username) {
		return user.hasRole("ROLE_ADMIN")
		       || user.getUsername().equals(username);
	}

	public boolean commentable(User curUser) {
		String curUsername = curUser.getUsername();
		String writer = this.user.getUsername();
		User lastWriter = comments.getLast().getUser();

		log.info("lastWriter.hasRole() : {}", lastWriter.hasRole("ROLE_ADMIN"));
		return curUser.hasRole("ROLE_ADMIN")
		       || (writer.equals(curUsername)
		           && !lastWriter.getUsername().equals(curUsername));
	}
}
