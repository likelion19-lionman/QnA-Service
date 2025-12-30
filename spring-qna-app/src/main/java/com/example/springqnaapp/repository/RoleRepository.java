package com.example.springqnaapp.repository;

import com.example.springqnaapp.domain.Role;
import com.example.springqnaapp.domain.RoleEnum;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoleRepository extends Repository<Role, Long> {
    Role save(Role role);
	Optional<Role> findByRole(RoleEnum roleEnum);
}
