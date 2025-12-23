package com.example.springqnaapp.security;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {
	private final Object principal;
	private final Object credentials;

	public JwtAuthentication(@Nullable Collection<? extends GrantedAuthority> authorities,
	                         Object principal,
	                         Object credentials) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(true);
	}
}