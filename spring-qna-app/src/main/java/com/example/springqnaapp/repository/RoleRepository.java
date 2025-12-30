package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(RoleEnum roleEnum);
}