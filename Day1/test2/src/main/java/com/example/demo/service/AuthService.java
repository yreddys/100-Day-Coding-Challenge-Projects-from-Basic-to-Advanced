package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.JwtService;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;

@Service
public class AuthService {

	private final UserRepository repository;
	private final PasswordEncoder encoder;
	private final AuthenticationManager authenticationManager;
	private final CustomUserDetailsService userDetailsService;
	private final JwtService jwtService;

	public AuthService(UserRepository repository, PasswordEncoder encoder, AuthenticationManager authenticationManager,
			CustomUserDetailsService userDetailsService, JwtService jwtService) {
		this.repository = repository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
	}

	public String register(RegisterRequest request) {
		if (request.username() == null || request.username().isBlank() || request.email() == null
				|| request.email().isBlank() || request.password() == null || request.password().length() < 8) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Enter username, email and a password of at least 8 characters");
		}

		if (repository.existsByUsername(request.username())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
		}

		if (repository.existsByEmail(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

		User user = new User();
		user.setUsername(request.username().trim());
		user.setEmail(request.email().trim());
		user.setPassword(encoder.encode(request.password()));
		user.setRole("USER"); // Never accept the role from registration.

		repository.save(user);
		return "Registration successful";
	}

	public AuthResponse login(LoginRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		UserDetails user = userDetailsService.loadUserByUsername(request.username());

		String role = user.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

		return new AuthResponse(jwtService.generateToken(user), user.getUsername(), role);
	}
}
