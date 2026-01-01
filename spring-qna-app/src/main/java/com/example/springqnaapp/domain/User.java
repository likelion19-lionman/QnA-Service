package com.example.springqnaapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "username", "email" })
@Slf4j
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            orphanRemoval = false
    )
    private List<Qna> qnas = new ArrayList<>();

	@OneToMany(
			mappedBy = "user",
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			orphanRemoval = false
	)
	private List<Comment> comments = new ArrayList<>();

	@ManyToMany
	@JoinTable(
			name = "user_role",
			joinColumns = { @JoinColumn(name = "user_id", nullable = false) },
			inverseJoinColumns = { @JoinColumn(name = "role_id", nullable = false) }
	)
	private Set<Role> roles = new HashSet<>();

	public User(
            String username,
            String email,
            String password,
            Role role
    ) {
		this.username = username;
		this.email = email;
		this.password = password;
        this.roles.add(role);
	}

    public void addQna(Qna qna) {
        qnas.add(qna);
        qna.setUser(this);
    }

	public boolean hasRole(RoleEnum roleEnum) {
		log.info("hasRole(RoleEnum roleEnum), {}", roles.stream()
		                  .anyMatch(role -> role.getRole().equals(roleEnum)));
        return roles.stream()
                .anyMatch(role -> role.getRole().equals(roleEnum));
	}

	public boolean hasRole(String role) {
		log.info("들어온 변수: {}", role);
		for (Role iter : this.roles)
			log.info("가지고 있는 role: {}", iter);

		RoleEnum roleEnum = RoleEnum.findByRole(role);

		log.info("찾은 RoleEnum: {}", roleEnum);
		if (roleEnum != null) return hasRole(roleEnum);
		return false;
	}

	public Set<String> getStringRoles() {
		return roles.stream()
		            .map(Role::toString)
		            .collect(Collectors.toUnmodifiableSet());
	}
}