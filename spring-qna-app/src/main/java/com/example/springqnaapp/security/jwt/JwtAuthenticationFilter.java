package com.example.springqnaapp.security.jwt;

import com.example.springqnaapp.common.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenizer jwtTokenizer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        if (accessToken == null || accessToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtExceptionHandler(() -> authenticate(accessToken), request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

	private Set<GrantedAuthority> getAuthorities(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) claims.get("roles");
		return roles.stream()
		            .map(SimpleGrantedAuthority::new)
		            .collect(Collectors.toSet());
	}

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        return null;
    }

    private Authentication authenticate(String accessToken) throws JwtException, IllegalArgumentException {
        Claims claim = jwtTokenizer.parseAccessToken(accessToken);
        Set<GrantedAuthority> authorities = getAuthorities(claim);
        return new JwtAuthentication(authorities,
                                     claim.get("username", String.class),
                                     null);
    }

    public static <T> T jwtExceptionHandler(Supplier<T> func, HttpServletRequest request) {
        try {
            return func.get();
        } catch (ExpiredJwtException e) {
            request.setAttribute("error", JwtErrorCode.JWT_TOKEN_EXPIRED);
            throw new BadCredentialsException("Invalid token");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("error", JwtErrorCode.JWT_UNSUPPORTED);
            throw new BadCredentialsException("Invalid token");
        } catch (MalformedJwtException e) {
            request.setAttribute("error", JwtErrorCode.JWT_MALFORMED);
            throw new BadCredentialsException("Invalid token");
        } catch (PrematureJwtException e) {
            request.setAttribute("error", JwtErrorCode.JWT_NOT_YET_VALID);
            throw new BadCredentialsException("Invalid token");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", JwtErrorCode.JWT_ILLEGAL_ARGUMENT);
            throw new BadCredentialsException("Invalid token");
        }
    }
}