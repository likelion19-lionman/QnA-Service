package com.example.springqnaapp.domain;

public enum RoleEnum {
	ROLE_ADMIN,
	ROLE_USER;

	public static RoleEnum findByRole(String role) {
		for (RoleEnum roleEnum : values())
			if (roleEnum.toString().equals(role)) return roleEnum;
		return null;
	}
}
