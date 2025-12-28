package com.example.springqnaapp.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
public class JwtAuthentication implements Authentication {
	private final Object principal;
	private final Object credentials;
	@Setter
	private boolean authenticated;
	private final Set<? extends GrantedAuthority> authorities;

	public JwtAuthentication(@Nullable Set<? extends GrantedAuthority> authorities,
	                         Object principal,
	                         Object credentials) {
		this.authorities = authorities;
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(true);
	}

	@Override
	public @Nullable Object getDetails() {
		return null;
	}

	@Override
	public String getName() {
		return principal.toString();
	}
}