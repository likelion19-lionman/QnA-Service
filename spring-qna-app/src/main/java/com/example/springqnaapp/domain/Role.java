package com.example.springqnaapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@EqualsAndHashCode(of = "role")
@ToString(onlyExplicitlyIncluded = true)
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short id;

	@Enumerated(EnumType.STRING)
	@Getter
	@ToString.Include
	private RoleEnum role;

    public Role(RoleEnum role) {
        this.role = role;
    }
}