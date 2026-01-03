package com.example.springqnaapp.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public boolean hasRole(RoleEnum roleEnum) {
        return roles.stream()
                .anyMatch(role -> role.getRole().equals(roleEnum));
	}

	public boolean hasRole(String role) {
		RoleEnum roleEnum = RoleEnum.findByRole(role);
		if (roleEnum != null) return hasRole(roleEnum);
		return false;
	}

	public Set<String> getStringRoles() {
		return roles.stream()
		            .map(Role::toString)
		            .collect(Collectors.toUnmodifiableSet());
	}
}